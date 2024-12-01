document.addEventListener("DOMContentLoaded", function() {
    const cookieConsent = document.getElementById("cookieConsent");
    const acceptCookies = document.getElementById("acceptCookies");
    const declineCookies = document.getElementById("declineCookies");
    const moreInfoCookies = document.getElementById("moreInfoCookies");
    const bookSection = document.getElementById("bookSection");

    // Check if cookies have already been accepted
    if (getCookie("cookiesAccepted") === "true") {
        cookieConsent.style.display = "none"; // Hide the consent banner if cookies are accepted
        bookSection.style.display = "block"; // Show the book search section
    } else {
        cookieConsent.style.display = "block"; // Show the consent banner
        bookSection.style.display = "none"; // Hide the book search section until cookies are accepted
    }

    // Accept cookies and set a cookie in the browser
    acceptCookies.addEventListener("click", function() {
        setCookie("cookiesAccepted", "true", 365); // Store cookie consent in a cookie for 365 days
        cookieConsent.style.display = "none"; // Hide the banner
        bookSection.style.display = "block"; // Show the book search section
    });

    // Decline cookies and set a cookie for declined consent
    declineCookies.addEventListener("click", function() {
        setCookie("cookiesAccepted", "false", 365); // Store that the user declined
        cookieConsent.style.display = "none"; // Hide the banner
        alert("You must accept cookies to use the site.");
        // Optionally redirect the user or disable further actions here
    });

    // More info about cookies
    moreInfoCookies.addEventListener("click", function() {
        alert("Cookies help us to provide a better service. For more details, please refer to our Privacy Policy.");
    });
});

// Function to set a cookie
function setCookie(name, value, days) {
    const d = new Date();
    d.setTime(d.getTime() + (days * 24 * 60 * 60 * 1000)); // Expire in 'days' days
    let expires = "expires=" + d.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/"; // Set cookie
}

// Function to get a cookie value
function getCookie(name) {
    let nameEq = name + "=";
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i].trim();
        if (c.indexOf(nameEq) === 0) {
            return c.substring(nameEq.length, c.length);
        }
    }
    return "";
}

// Search for books from Google Books API
document.getElementById("searchForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent the default form submission
    let title = document.getElementById("titleInput").value; // Get the title input value

    // Fetch book data from Google Books API
    fetch(`https://www.googleapis.com/books/v1/volumes?q=${title}`)
        .then(response => response.json()) // Parse the JSON response
        .then(data => {
            displaySearchResults(data.items); // Display the search results
        })
        .catch(error => console.error('Error:', error)); // Log any errors
});

// Function to display search results
function displaySearchResults(books) {
    const resultsDiv = document.getElementById("searchResults");
    resultsDiv.innerHTML = ''; // Clear previous results

    if (!books) {
        resultsDiv.innerHTML = '<p>No results found.</p>'; // Display message if no books found
        return;
    }

    // Iterate over each book and display its details
    books.forEach(book => {
        const bookDiv = document.createElement("div");
        bookDiv.classList.add('bookItem');
        bookDiv.innerHTML = `
            <h4>${book.volumeInfo.title}</h4>
            <p>Author(s): ${book.volumeInfo.authors.join(", ")}</p>
            <p>Description: ${book.volumeInfo.description || 'No description available.'}</p>
        `;
        resultsDiv.appendChild(bookDiv); // Add the book to the results
    });
}
