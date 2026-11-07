async function api(url, method = "GET", body) {
    const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    if (!response.ok) {
        const problem = await response.json();
        alert(problem.errors ? problem.errors.join("\n") : problem.detail);
        throw new Error(problem.detail);
    }
    return response.status === 204 ? null : response.json();
}

function esc(text) {
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
}

async function loadPosts() {
    const posts = await api("/posts");
    let html = "";
    for (const post of posts) {
        const comments = await api(`/posts/${post.id}/comments`);
        html += `
            <div class="post">
                <h2>${esc(post.title)}</h2>
                <small>${esc(post.author)}</small>
                <p>${esc(post.content)}</p>
                <button onclick="editPost(${post.id})">Редактировать</button>
                <button onclick="deletePost(${post.id})">Удалить</button>
                <h3>Комментарии</h3>
                ${comments.map(c => `
                    <div>${esc(c.author)}: ${esc(c.text)}
                        <button onclick="deleteComment(${c.id})">✕</button>
                    </div>`).join("")}
                <form onsubmit="addComment(event, ${post.id})">
                    <input name="author" placeholder="Автор">
                    <input name="text" placeholder="Комментарий">
                    <button>Добавить</button>
                </form>
            </div>`;
    }
    document.getElementById("posts").innerHTML = html;
}

async function createPost(event) {
    event.preventDefault();
    const form = event.target;
    await api("/posts", "POST", {
        title: form.elements.title.value,
        content: form.elements.content.value,
        author: form.elements.author.value
    });
    form.reset();
    loadPosts();
}

async function editPost(id) {
    const post = await api(`/posts/${id}`);
    const title = prompt("Заголовок", post.title);
    const content = prompt("Текст", post.content);
    if (title === null || content === null) return;
    await api(`/posts/${id}`, "PUT", { title, content, author: post.author });
    loadPosts();
}

async function deletePost(id) {
    if (!confirm("Удалить пост вместе с комментариями?")) return;
    await api(`/posts/${id}`, "DELETE");
    loadPosts();
}

async function addComment(event, postId) {
    event.preventDefault();
    const form = event.target;
    await api(`/posts/${postId}/comments`, "POST", {
        author: form.elements.author.value,
        text: form.elements.text.value
    });
    loadPosts();
}

async function deleteComment(id) {
    await api(`/comments/${id}`, "DELETE");
    loadPosts();
}

loadPosts();
