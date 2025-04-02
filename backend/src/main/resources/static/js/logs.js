let username = null;
let userInfo = sessionStorage.getItem("userInfo");
let tBancoHoras = document.getElementById("tBancoHoras").querySelector("tbody");
let tLogs = document.getElementById("tLogs").querySelector("tbody");

if (userInfo) {
    const user = JSON.parse(userInfo);
    username = user.sub;
    console.log(username)
}

window.addEventListener("load", function() {
    console.log("Página carregada!");
    fetch(`http://client.local:8080/home/${username}/expedientes`, { method: "GET" })
    .then(response =>{
        if(!response.ok){
            throw new Error(response.status);
        }
        return response.json();
    })
    .then(data => {
        data.forEach(item => {
            let data = item.data;
            let horas = format(item.horas);

            
            let novaLinha = document.createElement("tr");
            novaLinha.innerHTML = `
                <td>${data}</td>
                <td>${horas}</td>
            `;

            tBancoHoras.appendChild(novaLinha);
        });
    })
    .catch(error => {
        console.error('Erro ao buscar os dados:', error);
    });
});

window.addEventListener("load", function() {
    fetch(`http://client.local:8080/home/${username}/registros`, { method: "GET" })
    .then(response =>{
        if(!response.ok){
            throw new Error(response.status);
        }
        return response.json();
    })
    .then(data => {
        data.forEach(item => {
            let data = item.data;
            let hora = item.hora;
            let tipo = item.tipo;

            
            let novaLinha = document.createElement("tr");
            novaLinha.innerHTML = `
                <td>${data}</td>
                <td>${hora}</td>
                <td>${tipo}</td>
            `;

            tLogs.appendChild(novaLinha);
        });
    })
    .catch(error => {
        console.error('Erro ao buscar os dados:', error);
    });
});

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