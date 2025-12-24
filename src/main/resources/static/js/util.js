const BASE_URL = "";

function getToken() {
    return localStorage.getItem("token");
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}

function parseJwt(token) {
    const base64 = token.split('.')[1];
    return JSON.parse(atob(base64));
}

function isAdmin() {
    const token = getToken();
    if (!token) return false;
    const payload = parseJwt(token);
    return payload.role === "ADMIN";
}
