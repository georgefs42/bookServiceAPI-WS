// Handle login form submission
document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form from submitting normally

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Prepare data to be sent in the request
    const loginData = new URLSearchParams();
    loginData.append('username', username);
    loginData.append('password', password);

    // Send POST request to the backend login API
    fetch('http://localhost:8080/users/login', {
        method: 'POST',
        body: loginData,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    })
    .then(response => {
        return response.json(); // Parse the JSON response
    })
    .then(data => {
        if (data.message === "Login successful") {
            // Handle successful login
            alert('Login successful');
            window.location.href = '/login/adminDashboard.html'; // Redirect to another page (e.g., user dashboard)
        } else {
            // Handle error
            document.getElementById('error-message').style.display = 'block';
            document.getElementById('error-message').innerText = data.message;
        }
    })
    .catch(error => {
        // Handle any other errors
        document.getElementById('error-message').style.display = 'block';
        document.getElementById('error-message').innerText = "An unexpected error occurred.";
    });
});
