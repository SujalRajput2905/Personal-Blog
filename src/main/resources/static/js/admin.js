const role = localStorage.getItem("role");
if (role !== "ADMIN") {
    alert("Access denied");
    window.location.href = "posts.html";
}

document.addEventListener("DOMContentLoaded", () => {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Unauthorized");
        window.location.href = "login.html";
        return;
    }

    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const postsDiv = document.getElementById("adminPosts");

    function loadPosts() {
        fetch(BASE_URL + "/posts", {
            headers: {
                Authorization: "Bearer " + token
            }
        })
        .then(res => {
            if (!res.ok) throw new Error("Failed to load posts");
            return res.json();
        })
        .then(posts => {
            postsDiv.innerHTML = "";

            if (posts.length === 0) {
                postsDiv.innerHTML = "<p>No posts available</p>";
                return;
            }

            posts.forEach(p => {
                const d = document.createElement("div");
                d.className = "post";

                d.innerHTML = `
                    <h3 class="post-title">${p.title}</h3>
                    <p class="post-content">${p.content}</p>

                    <div class="actions">
                        <button class="edit-btn" onclick="enableEdit('${p.id}')">Edit</button>
                        <button class="delete-btn" onclick="deletePost('${p.id}')">Delete</button>
                    </div>

                    <div class="edit-form" id="edit-${p.id}" style="display:none;">
                        <input id="edit-title-${p.id}" value="${p.title}">
                        <textarea id="edit-content-${p.id}">${p.content}</textarea>

                        <button class="primary-btn" onclick="updatePost('${p.id}')">Save</button>
                        <button class="secondary-btn" onclick="cancelEdit('${p.id}')">Cancel</button>
                    </div>
                `;

                postsDiv.appendChild(d);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Could not load posts");
        });
    }

    window.createPost = function () {
        if (!titleInput.value || !contentInput.value) {
            alert("Title and content are required");
            return;
        }

        fetch(BASE_URL + "/posts", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify({
                title: titleInput.value,
                content: contentInput.value
            })
        })
        .then(res => {
            if (!res.ok) throw new Error("Create failed");
            titleInput.value = "";
            contentInput.value = "";
            loadPosts();
        })
        .catch(() => alert("Create failed"));
    };
    window.enableEdit = function (id) {
        document.getElementById(`edit-${id}`).style.display = "block";
    };

    window.cancelEdit = function (id) {
        document.getElementById(`edit-${id}`).style.display = "none";
    };

    window.updatePost = function (id) {
        const title = document.getElementById(`edit-title-${id}`).value;
        const content = document.getElementById(`edit-content-${id}`).value;

        if (!title || !content) {
            alert("Title and content required");
            return;
        }

        fetch(BASE_URL + `/posts/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify({ title, content })
        })
        .then(res => {
            if (!res.ok) throw new Error("Update failed");
            loadPosts();
        })
        .catch(() => alert("Update failed"));
    };

    window.deletePost = function (id) {
        fetch(BASE_URL + `/posts/${id}`, {
            method: "DELETE",
            headers: {
                Authorization: "Bearer " + token
            }
        })
        .then(res => {
            if (!res.ok) throw new Error("Delete failed");
            loadPosts();
        })
        .catch(() => alert("Delete failed"));
    };

    loadPosts();
});
