const NotesApp = (function () {

    const DOM = {
        activeNoteId: document.getElementById('activeNoteId'),
        activeNoteTitle: document.getElementById('activeNoteTitle'),
        activeNoteContent: document.getElementById('activeNoteContent'),
        activeNoteForm: document.getElementById('activeNoteForm')
    }

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    function bindEvents() {
        document.querySelectorAll('.note-item').forEach(item => {
            item.addEventListener('click', (e) => {
                switchNote(item);
            })
        })
        DOM.activeNoteForm.addEventListener('submit', e => {
            e.preventDefault();
            saveNote();
        })
    }

    async function switchNote(element) {
        const noteId = element.getAttribute('id');

        try {
            const response = await fetch(`/api/notes/${noteId}`);
            if (!response.ok) throw new Error('Не удалось загрузить заметку');

            const note = await response.json();

            DOM.activeNoteId.value = note.id;
            DOM.activeNoteTitle.value = note.title;
            DOM.activeNoteContent.value = note.content;
            DOM.activeNoteForm.action = `/notes/save/${note.id}`;
            DOM.activeNoteForm.action = `/notes/new`;

            document.querySelectorAll('.note-item').forEach(item => {
                item.classList.remove('active');
            });
            element.classList.add('active');

        } catch (error) {
            console.error('Ошибка:', error);
            alert('Не удалось загрузить заметку. Проверьте контроллер.');
        }
    }

    async function saveNote() {

        const id = DOM.activeNoteId.value;
        const title = DOM.activeNoteTitle.value;
        const content = DOM.activeNoteContent.value;

        // Определяем URL: если ID есть — обновляем, если нет — создаем новую
        const url = id ? `/api/notes/${id}` : '/api/notes/new';
        const method = id ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
                },
                body: JSON.stringify({ id, title, content })
            });

            if (!response.ok) throw new Error('Ошибка сохранения');

        } catch (error) {
            console.error(error);
            alert('Не удалось сохранить');
        }
    }

    return {
        init: function () {
            bindEvents();
        }
    }
})();

NotesApp.init();