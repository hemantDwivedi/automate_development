$("#buildBtn").click(function() {
    $.ajax({
        url: '/api/build/start',
            type: 'GET',
        success: function(response) {
            console.log('Build started successfully:', response);
        },
        error: function(xhr, status, error) {
            console.error('Error starting build:', error);
        }
    });
});
