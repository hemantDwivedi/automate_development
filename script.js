$("#buildBtn").click(function() {
    $.ajax({
        url: 'api/build/start',
        type: 'POST',
        success: function(response) {
            console.log('Build started successfully:', response);
        },
        error: function(xhr, status, error) {
            console.error('Error starting build:', error);
        }
    });
});
