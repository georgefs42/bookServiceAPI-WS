const baseURL = 'http://localhost:8080/users';

// Function to create a new user
function createUser() {
    // Retrieve input values
    const username = document.getElementById('postUsername').value;
    const password = document.getElementById('postPassword').value;
    const roles = document.getElementById('postRoles').value.split(',').map(role => role.trim());

    // Input validation - check if all required fields are filled
    if (!username || !password || roles.length === 0) {
        document.getElementById('result').innerHTML = "All fields (Username, Password, Roles) are required!";
        return; // Stop execution if validation fails
    }

    // Create the user object
    const user = { username, password, roles };

    // Send the POST request to create the user
    fetch(baseURL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    })
    .then(response => response.json())
    .then(data => {
        // Success message
        document.getElementById('result').innerHTML = "User added successfully!";

        // Reset all labels and input fields
        resetForm();
    })
    .catch(error => {
        // Error handling
        document.getElementById('result').innerHTML = `Error: ${error}`;
    });
}

// Function to reset input fields and labels
function resetForm() {
    // Reset the input fields
    document.getElementById('postUsername').value = '';
    document.getElementById('postPassword').value = '';
    document.getElementById('postRoles').value = '';

    // Clear the result message
    document.getElementById('result').innerHTML = '';
}
