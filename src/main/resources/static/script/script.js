document.getElementById('generateButton').addEventListener('click', function () {
    const sessionId = 'userSessionId'; // Presumed to be fetched or generated elsewhere
    requestAccess(sessionId);
});

function requestAccess(sessionId) {
    fetch(`/request-access?sessionId=${sessionId}`)
        .then(response => response.text())
        .then(data => {
            if (data === "Access granted") {
                alert("You can generate the image now.");
                performImageGeneration();
            } else {
                alert(data);
                setTimeout(() => requestAccess(sessionId), 5000); // Retry after 5 seconds
            }
        });
}

function performImageGeneration() {
    var statusMessage = document.getElementById('statusMessage');
    var imageModal = document.getElementById('imageModal');
    var resultImage = document.getElementById('resultImage');
    var parameter = document.getElementById('parameterSelect').value;

    statusMessage.textContent = 'Please wait, generating image...';
    statusMessage.style.display = 'block';
    resultImage.style.display = 'none';

    fetch('/generate-image', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({parameter: parameter})
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to generate image');
            return response.blob();
        })
        .then(blob => {
            const imageUrl = URL.createObjectURL(blob);
            resultImage.src = imageUrl;
            resultImage.onload = function () {
                imageModal.style.display = 'block';
                resultImage.style.display = 'block';
                statusMessage.style.display = 'none';
            }
            releaseAccess();
        })
        .catch(error => {
            statusMessage.textContent = 'Error: ' + error.message;
            console.error('Error loading image:', error);
        });
}

function releaseAccess() {
    fetch(`/release-access`, {method: 'POST'});
}

// Setup close button functionalities
setupModalClosures();

function setupModalClosures() {
    // Handling close buttons for all modals
    document.querySelectorAll(".close").forEach(button => {
        button.onclick = function () {
            const modal = button.closest('.modal');
            modal.style.display = "none";
            const video = modal.querySelector('video');
            if (video) {
                video.pause();
                video.currentTime = 0;
            }
        };
    });

    // Handle clicking outside modals to close
    window.onclick = function (event) {
        document.querySelectorAll('.modal').forEach(modal => {
            if (event.target == modal) {
                modal.style.display = "none";
                const video = modal.querySelector('video');
                if (video) {
                    video.pause();
                    video.currentTime = 0;
                }
            }
        });
    };
}

document.getElementById('playButton').addEventListener('click', function () {
    const rickrollModal = document.getElementById('myModal');
    const video = document.getElementById('rickVideo');
    rickrollModal.style.display = "block";
    video.play();
});

document.querySelectorAll('.close').forEach(button => {
    button.onclick = function () {
        const modal = this.closest('.modal');
        modal.style.display = "none";
        if (modal.contains(document.getElementById('rickVideo'))) {
            const rickVideo = document.getElementById('rickVideo');
            rickVideo.pause();
            rickVideo.currentTime = 0;
        }
    };
});
