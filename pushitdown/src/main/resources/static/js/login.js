function exchangeCodeForToken(code) {
    fetch("http://auth-server.local:9000/oauth2/token", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ code })
    })
    .then(response => response.json())
    .then(data => {
        localStorage.setItem("token", data.access_token);
        console.log("Token JWT recebido:", data.access_token);
    })
    .catch(error => console.error("Erro ao trocar código por token:", error));
}

document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    
});
