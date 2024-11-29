const loginForm = document.getElementById('loginForm');
const errorMessage = document.getElementById('errorMessage');

// Handle form submission
loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();

  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  try {
    const response = await fetch('http://localhost:8080/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      throw new Error('Invalid username or password');
    }

    const data = await response.json();

    // Redirect based on role (Admin or User)
    if (data.role === 'ADMIN') {
      window.location.href = '/admin/dashboard.html';
    } else {
      window.location.href = '/user/dashboard.html';
    }
  } catch (error) {
    errorMessage.textContent = error.message;
  }
});
