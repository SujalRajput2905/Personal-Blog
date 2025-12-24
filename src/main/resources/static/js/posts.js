const token = localStorage.getItem("token");
const role = localStorage.getItem("role");

if (!token) {
    window.location.href = "login.html";
}

// Show admin button only for ADMIN
const adminBtn = document.getElementById("adminBtn");
if (adminBtn) {
    if (role === "ADMIN") {
        adminBtn.style.display = "inline-block";
    } else {
        adminBtn.style.display = "none";
    }
}

function goAdmin() {
    window.location.href = "admin.html";
}

// Load posts
fetch(BASE_URL + "/posts")
    .then(res => {
        if (!res.ok) throw new Error("Failed to load posts");
        return res.json();
    })
    .then(posts => {
        const container = document.getElementById("postsContainer");
        container.innerHTML = "";

        posts.forEach(p => {
            const div = document.createElement("div");
            div.className = "post-card";
            div.innerHTML = `
                <h3>${p.title}</h3>
                <p>${p.content}</p>

                <button class="like-btn"
                        onclick="toggleLike('${p.id}', this)">
                    ❤️ <span>${p.likedBy.length}</span>
                </button>
            `;
            container.appendChild(div);
        });
    })
    .catch(err => {
        console.error(err);
        alert("Could not load posts");
    });

// Logout
function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

function toggleLike(postId, btn) {
    fetch(BASE_URL + `/posts/${postId}/like`, {
        method: "POST",
        headers: {
            Authorization: "Bearer " + token
        }
    })
    .then(res => {
        if (!res.ok) throw new Error();
        return res.json();
    })
    .then(count => {
        btn.querySelector("span").innerText = count;
    })
    .catch(() => alert("Like failed"));
}

