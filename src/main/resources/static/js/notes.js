const NotesApp = (function () {
   const SaveStatus = {
      SAVING: "Saving...",
      SAVED: "All changes saved",
      ERROR: "No connection to the server",
   };

   const DOM = {
      activeNoteId: document.querySelector(".js-active-note-id"),
      activeNoteTitle: document.querySelector(".js-active-note-title"),
      activeNoteContent: document.querySelector(".js-active-note-content"),
      btnNoteDelete: document.querySelector(".js-note-delete"),
      btnNoteCreate: document.querySelector(".js-note-create"),
      notesList: document.querySelector(".js-notes-list"),
      saveIndicator: document.querySelector(".js-save-indicator"),
   };

   function getCookie(name) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) return parts.pop().split(";").shift();
   }

   const csrfToken = getCookie("XSRF-TOKEN");
   let noteId, noteSaveTimeout, shouldUpdateNotesList;

   function bindEvents() {
      DOM.btnNoteDelete.addEventListener("click", () => {
         if (!noteId) return;
         if (!confirm("Are you sure? " + DOM.activeNoteTitle.value)) return;
         deleteNote().then(() => {
            loadNotes();
         });
         clearActiveNote();
      });

      DOM.btnNoteCreate.addEventListener("click", () => {
         switchNote();
      });

      DOM.activeNoteContent.addEventListener("input", () => {
         saveNotePlan();
      });

      DOM.activeNoteTitle.addEventListener("input", () => {
         saveNotePlan();
         updateNoteTitle();
      });
   }

   function saveNotePlan() {
      clearTimeout(noteSaveTimeout);
      shouldUpdateNotesList = !noteId;
      updateSaveStatus(SaveStatus.SAVING);
      noteSaveTimeout = setTimeout(saveNote, 1500);
   }

   function updateNoteTitle() {
      const noteItem = document.querySelector(
         `.note-item[data-note-id="${noteId}"]`,
      );
      if (!noteItem) return;

      noteItem.textContent = DOM.activeNoteTitle.value;
   }

   async function switchNote(element) {
      // Saving active note if it necessary
      if (noteSaveTimeout) {
         clearTimeout(noteSaveTimeout);
         noteSaveTimeout = null;
         await saveNote();
      }

      if (!element) {
         clearActiveNote();
         return;
      }

      const selectedNoteId = element.dataset.noteId;

      try {
         const response = await fetch(`/api/notes/${selectedNoteId}`);
         if (!response.ok) throw new Error("Failed to load the note");

         const note = await response.json();

         noteId = note.id;
         DOM.activeNoteId.value = note.id;
         DOM.activeNoteTitle.value = note.title;
         DOM.activeNoteContent.value = note.content;

         document.querySelectorAll(".note-item").forEach((item) => {
            item.classList.remove("active");
         });
         element.classList.add("active");
      } catch (error) {
         console.error("Error:", error);
         alert("Failed to load the note");
      }
   }

   async function saveNote() {
      const data = {
         id: noteId,
         title: DOM.activeNoteTitle.value,
         content: DOM.activeNoteContent.value,
      };

      if (!data.title) return;

      console.log("saving... ", data);

      const url = noteId ? `/api/notes/${noteId}` : "/api/notes";
      const method = noteId ? "PUT" : "POST";

      try {
         const response = await fetch(url, {
            method: method,
            headers: {
               "Content-Type": "application/json",
               "X-XSRF-TOKEN": csrfToken,
            },
            body: JSON.stringify(data),
         });
         if (!response.ok) throw new Error("Failed to update notes");

         if (!noteId) {
            const note = await response.json();
            noteId = note.id;
         }

         if (shouldUpdateNotesList) {
            shouldUpdateNotesList = false;
            loadNotes();
         }

         updateSaveStatus(SaveStatus.SAVED);
      } catch (error) {
         console.error("Failed to save:", error);
         updateSaveStatus(SaveStatus.ERROR);
      }
   }

   async function deleteNote() {
      if (!noteId) return;

      const url = `/api/notes/${noteId}`;

      try {
         const response = await fetch(url, {
            method: "DELETE",
            headers: {
               "Content-Type": "application/json",
               "X-XSRF-TOKEN": csrfToken,
            },
         });

         if (!response.ok) throw new Error("Failed to save");

         noteId = null;
      } catch (error) {
         console.error(error);
         alert("Failed to save");
      }
   }

   async function loadNotes() {
      try {
         const response = await fetch("/api/notes");
         if (!response.ok) throw new Error("Error updating notes");

         const notes = await response.json();

         if (notes.length === 0) {
            DOM.notesList.innerHTML = `<div class="empty-state">Empty...\n:(</div>`;
            return;
         }

         const emptyState = DOM.notesList.querySelector(".empty-state");
         if (emptyState) emptyState.remove();

         const currentElements = DOM.notesList.querySelectorAll(".note-item");

         const currentElementsMap = new Map();
         currentElements.forEach((el) => {
            currentElementsMap.set(el.dataset.noteId, el);
         });

         const freshIds = new Set();

         notes.forEach((note) => {
            const stringId = String(note.id);
            freshIds.add(stringId);

            if (currentElementsMap.has(stringId)) {
               const existingEl = currentElementsMap.get(stringId);
               if (existingEl.textContent !== note.title) {
                  existingEl.textContent = note.title;
               }
            } else {
               const newNoteLink = document.createElement("a");
               newNoteLink.className = "note-item";
               newNoteLink.textContent = note.title;
               newNoteLink.dataset.noteId = stringId;
               newNoteLink.addEventListener("click", (e) => {
                  switchNote(newNoteLink);
               });

               console.log("new note add ", note.id, noteId);
               if (note.id === noteId) {
                  newNoteLink.classList.add("active");
               }

               DOM.notesList.appendChild(newNoteLink);
            }
         });

         currentElements.forEach((el) => {
            if (!freshIds.has(el.dataset.noteId)) {
               el.remove();
            }
         });
      } catch (error) {
         console.error("Failed to update note titles:", error);
      }
   }

   function clearActiveNote() {
      noteId = null;
      DOM.activeNoteId.value = "";
      DOM.activeNoteTitle.value = "";
      DOM.activeNoteContent.value = "";
      DOM.notesList.querySelectorAll(".note-item").forEach((item) => {
         item.classList.remove("active");
      });
   }

   function updateSaveStatus(saveStatus) {
      DOM.saveIndicator.textContent = saveStatus;
   }

   return {
      init: function () {
         bindEvents();
         loadNotes();
      },
   };
})();

NotesApp.init();
