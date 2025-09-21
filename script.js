document.getElementById('buildBtn').addEventListener('click', () => {
    const evtSource = new EventSource('/api/build/run?action=CLEAN_INSTALL');

    const log = document.getElementById('log');
    evtSource.onmessage = function(e) {
        log.textContent += e.data + '\n';
    };
    evtSource.onerror = function(e) {
        log.textContent += '[error] connection closed\n';
        evtSource.close();
    };
});