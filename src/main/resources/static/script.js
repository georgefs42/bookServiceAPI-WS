// Search for books from Google Books API
document.getElementById("searchForm").addEventListener("submit", function(event) {
    event.preventDefault();
    let title = document.getElementById("titleInput").value;

    fetch(`https://www.googleapis.com/books/v1/volumes?q=${title}`)
        .then(response => response.json())
        .then(data => {
            displaySearchResults(data.items);
        })
        .catch(error => console.error('Error:', error));
});

function displaySearchResults(books) {
    const resultsDiv = document.getElementById("searchResults");
    resultsDiv.innerHTML = ''; // Clear previous results

    if (!books) {
        resultsDiv.innerHTML = '<p>No results found.</p>';
        return;
    }

    books.forEach(book => {
        const bookDiv = document.createElement("div");
        bookDiv.innerHTML = `
            <h4>${book.volumeInfo.title}</h4>
            <p>Author(s): ${book.volumeInfo.authors.join(", ")}</p>
            <p>Description: ${book.volumeInfo.description || "No description available."}</p>
            <p>Published Date: ${book.volumeInfo.publishedDate}</p>
            <button onclick="fillBookForm('${book.volumeInfo.title}', '${book.volumeInfo.authors.join(", ")}', '${book.volumeInfo.description}', '${book.volumeInfo.publishedDate}', '${book.id}')">Add to Local Database</button>
        `;
        resultsDiv.appendChild(bookDiv);
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
    event.preventDefault();

    let book = {
        title: document.getElementById("bookTitleInput").value,
        author: document.getElementById("bookAuthorInput").value,
        description: document.getElementById("bookDescriptionInput").value,
        publishedDate: document.getElementById("bookPublishedDateInput").value,
        googleBookId: document.getElementById("bookGoogleIdInput").value,
    };

    fetch('http://localhost:8080/books', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        alert('Book saved!');
        loadSavedBooks(); // Refresh the saved books list
    })
    .catch(error => console.error('Error:', error));
});

// Load saved books from the database
function loadSavedBooks() {
    fetch('http://localhost:8080/books')
        .then(response => response.json())
        .then(data => {
            displaySavedBooks(data);
        })
        .catch(error => console.error('Error:', error));
}

function displaySavedBooks(books) {
    const savedBooksDiv = document.getElementById("savedBooks");
    savedBooksDiv.innerHTML = ''; // Clear previous saved books

    if (!books.length) {
        savedBooksDiv.innerHTML = '<p>No saved books found.</p>';
        return;
    }

    books.forEach(book => {
        const bookDiv = document.createElement("div");
        bookDiv.innerHTML = `
            <h4>${book.title}</h4>
            <p>Author(s): ${book.author}</p>
            <p>Description: ${book.description}</p>
            <p>Published Date: ${book.publishedDate}</p>
            <button onclick="fillUpdateForm(${book.id}, '${book.title}', '${book.author}', '${book.description}', '${book.publishedDate}', '${book.googleBookId}')">Update</button>
        `;
        savedBooksDiv.appendChild(bookDiv);
    });
}

// Fill update form with existing book data
function fillUpdateForm(id, title, author, description, publishedDate, googleBookId) {
    document.getElementById("bookIdToUpdate").value = id;
    document.getElementById("bookTitleUpdateInput").value = title;
    document.getElementById("bookAuthorUpdateInput").value = author;
    document.getElementById("bookDescriptionUpdateInput").value = description;
    document.getElementById("bookPublishedDateUpdateInput").value = publishedDate;
    document.getElementById("bookGoogleIdUpdateInput").value = googleBookId;
}

// Update a book (PUT)
document.getElementById("updateForm").addEventListener("submit", function(event) {
    event.preventDefault();

    let bookId = document.getElementById("bookIdToUpdate").value;
    let updatedBook = {
        title: document.getElementById("bookTitleUpdateInput").value,
        author: document.getElementById("bookAuthorUpdateInput").value,
        description: document.getElementById("bookDescriptionUpdateInput").value,
        publishedDate: document.getElementById("bookPublishedDateUpdateInput").value,
        googleBookId: document.getElementById("bookGoogleIdUpdateInput").value,
    };

    fetch(`http://localhost:8080/books/${bookId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedBook)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        alert('Book updated!');
        loadSavedBooks(); // Refresh the saved books list
    })
    .catch(error => console.error('Error:', error));
});

// Delete a book by ID (DELETE)
document.getElementById("deleteForm").addEventListener("submit", function(event) {
    event.preventDefault();
    let bookId = document.getElementById("bookIdToDelete").value;

    fetch(`http://localhost:8080/books/${bookId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            alert('Book deleted!');
            loadSavedBooks(); // Refresh the saved books list
        } else {
            alert('Error deleting book.');
        }
    })
    .catch(error => console.error('Error:', error));
});

// Load saved books on page load
window.onload = loadSavedBooks;
