// Sign-out and go back to index.html
document.getElementById("logoutButton").addEventListener("click", function() {
    // Clear session or any related sign-in data (this is usually handled by the backend,
    // but we can simulate by redirecting the user to logout endpoint)
    fetch('/logout', {
        method: 'POST',  // Use POST to trigger the logout
        credentials: 'same-origin',  // Include cookies for session handling
    })
    .then(response => {
        if (response.ok) {
            window.location.href = "/index.html";  // Redirect to index.html after logout
        }
    })
    .catch(error => console.error('Error during logout:', error));
});

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
            <p>Description: ${book.volumeInfo.description || "No description available."}</p>
            <p>Published Date: ${book.volumeInfo.publishedDate}</p>
            <button onclick="fillBookForm('${book.volumeInfo.title}', '${book.volumeInfo.authors.join(", ")}', '${book.volumeInfo.description}', '${book.volumeInfo.publishedDate}', '${book.id}')">Add to Local Database</button>
        `;
        resultsDiv.appendChild(bookDiv); // Append the book div to results
    });
}

// Fill the form with selected book details
function fillBookForm(title, authors, description, publishedDate, googleBookId) {
    document.getElementById("bookTitleInput").value = title;
    document.getElementById("bookAuthorInput").value = authors;
    document.getElementById("bookDescriptionInput").value = description;
    document.getElementById("bookPublishedDateInput").value = publishedDate;
    document.getElementById("bookGoogleIdInput").value = googleBookId;
}

// Save a book (POST)
document.getElementById("bookForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent default form submission

    let book = {
        title: document.getElementById("bookTitleInput").value,
        author: document.getElementById("bookAuthorInput").value,
        description: document.getElementById("bookDescriptionInput").value,
        publishedDate: document.getElementById("bookPublishedDateInput").value,
        googleBookId: document.getElementById("bookGoogleIdInput").value,
    };

    // Send a POST request to save the book
    fetch('http://localhost:8080/books', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book) // Convert the book object to JSON
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json(); // Parse the response as JSON
    })
    .then(data => {
        alert('Book saved!'); // Alert user on successful save
        loadSavedBooks(); // Refresh the saved books list
    })
    .catch(error => console.error('Error:', error)); // Log any errors
});

// Load saved books from the database
function loadSavedBooks() {
    fetch('http://localhost:8080/books')
        .then(response => response.json()) // Parse the JSON response
        .then(data => {
            displaySavedBooks(data); // Display the saved books
        })
        .catch(error => console.error('Error:', error)); // Log any errors
}

// Function to display saved books
function displaySavedBooks(books) {
    const savedBooksDiv = document.getElementById("savedBooks");
    savedBooksDiv.innerHTML = ''; // Clear previous saved books

    if (!books.length) {
        savedBooksDiv.innerHTML = '<p>No saved books found.</p>'; // Display message if no saved books
        return;
    }

    // Iterate over each saved book and display its details
    books.forEach(book => {
        const bookDiv = document.createElement("div");
        bookDiv.classList.add('bookItem');
        bookDiv.innerHTML = `
            <h4>${book.title}</h4>
            <p>Author(s): ${book.author}</p>
            <p>Description: ${book.description}</p>
            <p>Published Date: ${book.publishedDate}</p>
        `;
        savedBooksDiv.appendChild(bookDiv); // Append the book div to saved books
    });
}

// Update a book (PUT)
document.getElementById("updateForm").addEventListener("submit", function(event) {
    event.preventDefault();

    let updatedBook = {
        title: document.getElementById("bookTitleUpdateInput").value,
        author: document.getElementById("bookAuthorUpdateInput").value,
        description: document.getElementById("bookDescriptionUpdateInput").value,
        publishedDate: document.getElementById("bookPublishedDateUpdateInput").value,
        googleBookId: document.getElementById("bookGoogleIdUpdateInput").value,
    };

    let bookId = document.getElementById("bookIdToUpdate").value;

    fetch(`http://localhost:8080/books/${bookId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedBook)
    })
    .then(response => {
        if (response.ok) {
            alert('Book updated successfully');
            loadSavedBooks(); // Refresh the saved books list
        } else {
            alert('Error updating book');
        }
    })
    .catch(error => console.error('Error:', error));
});

// Delete a book (DELETE)
document.getElementById("deleteForm").addEventListener("submit", function(event) {
    event.preventDefault();

    let bookId = document.getElementById("bookIdToDelete").value;

    fetch(`http://localhost:8080/books/${bookId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            alert('Book deleted successfully');
            loadSavedBooks(); // Refresh the saved books list
        } else {
            alert('Error deleting book');
        }
    })
    .catch(error => console.error('Error:', error));
});
