let username = null;

document.getElementById("bEntrar").addEventListener("click", startOrReset);
document.getElementById("bSair").addEventListener("click", logout);
document.getElementById("bHistorico").addEventListener("click", ()=>{
    window.open("http://client.local:8080/historico", "popup", "width=800,height=600");
});

document.addEventListener("DOMContentLoaded", function () {
    const token = sessionStorage.getItem("tokenData");
    const userInfo = sessionStorage.getItem("userInfo"); // Retrieve user info from sessionStorage

    if (!token || !userInfo) {
        window.location.href = "client.local:8080/redirect";
    } 
    else {
        // Parse the user info JSON string into an object
        const user = JSON.parse(userInfo);

        // Log user info to the console
        console.log("Usuário autenticado com token:", token);
        console.log("Informações do usuário:", user);

        username = user.sub;
        // Example: Display user ID (sub) on the page
        document.getElementById("user").textContent = `${user.sub}`;
    }
});

let inicio = 0;
let tempoTotal = 0;
let intervalo = null;
let started = false;

window.onload = async function() {
    if (localStorage.getItem("started") === "true") {
        document.getElementById("iPicaPau").style.opacity = "0";
        setTimeout(() => {
            document.getElementById("lPicaPau").textContent = "Pica Pau foi limpar navio"
        }, 1000); // Tempo da transição (1s) 

        document.getElementById("iPernalonga").style.opacity = "0";
        setTimeout(() => {
            document.getElementById("lPernalonga").textContent = "Pernalonga foi pra esquina"
        }, 1000); // Tempo da transição (1s) 

        document.getElementById("bEntrar").textContent = "Sair";

        console.log(document.getElementById("lTimer").textContent);

        if (localStorage.getItem("tempoTotal") != null){
            tempoTotal = localStorage.getItem("tempoTotal");
        }

        start(); // Agora start() só será chamado após refreshTime() terminar
    }
    else{
        console.log("Não estava rodando")
        await refreshTime(); // Aguarda a atualização do tempo antes de continuar
    }
};

async function refreshTime() {
    console.log("refresh!")
    try {
        const response = await fetch(`http://client.local:8080/home/${username}/total-horas`, { method: "GET" });

        if (!response.ok) {
            throw new Error(`Erro HTTP! Status: ${response.status}`);
        }

        const data = await response.json();

        tempoTotal = data;
        document.getElementById("lTimer").textContent = format(data);
        console.log(document.getElementById("lTimer").textContent);
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
}

function logout() {
    fetch("http://client.local:8080/logout", {
        method: "POST",
        credentials: "include"
    }).then(() => {
        window.location.href = "http://auth-server.local:9000/logout?post_logout_redirect_uri=http://client.local:8080/login";
    }).catch(error => console.error("Logout failed:", error));
}

function startOrReset(){
    if(!started){
        fetch(`http://client.local:8080/home/${username}/entrance`, {
            method: "POST"
        })
        .then(async response => {
            if (!response.ok) {
                // Tenta pegar a mensagem de erro do backend
                const errorMessage = await response.text(); // Ou use response.json() se for JSON
                throw new Error(`${errorMessage}`);
            }
            return response.text(); // Se a resposta for texto
        })
        .then(data => {
            document.getElementById("iPicaPau").style.opacity = "0";
            setTimeout(() => {
                document.getElementById("lPicaPau").textContent = "Pica Pau foi limpar navio"
            }, 1000); // Tempo da transição (1s) 

            document.getElementById("iPernalonga").style.opacity = "0";
            setTimeout(() => {
                document.getElementById("lPernalonga").textContent = "Pernalonga foi pra esquina"
            }, 1000); // Tempo da transição (1s) 

            document.getElementById("bEntrar").textContent = "Sair"
            localStorage.setItem("started", "true");
            start();
        })
        .catch(error => {
            alert(error.message)
        });
    }
    else{
        fetch(`http://client.local:8080/home/${username}/exit`, {
            method: "POST"
        })
        .then(async response => {
            if (!response.ok) {
                // Tenta pegar a mensagem de erro do backend
                const errorMessage = await response.text(); // Ou use response.json() se for JSON
                throw new Error(`Erro HTTP! Status: ${response.status} - ${errorMessage}`);
            }
            return response.text(); // Se a resposta for texto
        })
        .then(data => {
            document.getElementById("iPicaPau").style.opacity = "1";
            setTimeout(() => {
                document.getElementById("lPicaPau").textContent = "Pica Pau foi dormir"
            }, 1000); // Tempo da transição (1s) 

            document.getElementById("iPernalonga").style.opacity = "1";
            setTimeout(() => {
                document.getElementById("lPernalonga").textContent = "Pernalonga foi pra praia"
            }, 1000); // Tempo da transição (1s) 

            document.getElementById("bEntrar").textContent = "Entrar"
            started = false;

            localStorage.removeItem("started");

            clearInterval(intervalo);
        })
        .catch(error => {
            alert(error.message)
        });
    }
}
function format(time){
    let seconds = Math.floor(time/1000);
    let h = Math.floor(seconds/3600);
    let m = Math.floor((seconds % 3600)/60);
    let s = Math.floor(seconds % 60);

    return (
        (h < 10 ? "0" : "") + h + ":" +
        (m < 10 ? "0" : "") + m + ":" +
        (s < 10 ? "0" : "") + s
    );
}

function start(){
    started = true;
    inicio = Date.now() - tempoTotal;
    console.log(document.getElementById("lTimer").textContent)
    intervalo = setInterval(() =>{
        let now = Date.now() - inicio;

        document.getElementById("lTimer").textContent = format(now);
        localStorage.setItem("tempoTotal", now);
    }, 10);
}