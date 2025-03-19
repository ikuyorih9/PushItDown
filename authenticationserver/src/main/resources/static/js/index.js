async function exchangeCodeForToken(code) {
    const response = await fetch("http://localhost:8080/oauth2/token", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            grant_type: "authorization_code",
            client_id: "pushitdown-client",
            client_secret: "secret",
            code: code,
            redirect_uri: "http://localhost:8080/login/oauth2/code/pushitdown-client"
        })
    });

    const data = await response.json();
    localStorage.setItem("access_token", data.access_token);
}

async function baterPonto() {
    const token = localStorage.getItem("access_token");

    const response = await fetch("http://localhost:8080/api/ponto/entrada", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        alert("Ponto batido com sucesso!");
    } else {
        alert("Erro ao bater ponto.");
    }
}