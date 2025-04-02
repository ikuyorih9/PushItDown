function obterCodigoDeAutorizacao() {
    window.location.href = "http://auth-server.local:9000/oauth2/authorize?response_type=code&client_id=pushitdown";
}

function trocarCodigoPorToken() {
    const urlParams = new URLSearchParams(window.location.search);
    const authorizationCode = urlParams.get("code");

    if (!authorizationCode) {
        console.error("Código de autorização não encontrado!");
        return;
    }

    fetch("http://auth-server.local:9000/oauth2/token", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            code: authorizationCode,
            client_id: "pushitdown",
            client_secret: "supersecret"
        })
    })
    .then(response => response.json())
    .then(data => {
        localStorage.setItem("token", data.access_token); // Armazena o token JWT
        console.log("Token JWT recebido:", data.access_token);
        window.location.href = "home.html"; // Redireciona para a página principal
    })
    .catch(error => console.error("Erro ao trocar código por token:", error));
}

// Chama essa função automaticamente ao carregar a página de callback
window.onload = trocarCodigoPorToken;