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
        `;
        resultsDiv.appendChild(bookDiv); // Append the book div to results
    });
}