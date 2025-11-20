function validarAltaPerfil() {
    // Obtenemos los ELEMENTOS (no solo el valor) para verificar sus clases CSS
    const inputNick = document.getElementById("nick");
    const inputCorreo = document.getElementById("correo");

    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const contrasenia = document.getElementById("contrasenia").value;
    const fechaNac = document.getElementById("fechaNac").value;
    const dirImagen = document.getElementById("dirImagen").value;
    const direccion = document.getElementById("direccion").value;
    const tipoUsuario = document.querySelectorAll('input[name="tipoUsuario"]');


    if (inputNick.classList.contains("is-invalid")) {
        alert("El nickname ya está en uso. Por favor, elige otro.");
        inputNick.focus();
        return false;
    }


    if (inputCorreo.classList.contains("is-invalid")) {
        alert("El correo electrónico ya está registrado.");
        inputCorreo.focus();
        return false;
    }

    if (!validarCampoVacio(inputNick.value, "nick")) return false;
    if (!validarCampoVacio(nombre, "nombre")) return false;
    if (!validarCampoVacio(apellido, "apellido")) return false;
    if (!validarCampoVacio(contrasenia, "contrasenia")) return false;
    if (!validarCampoVacio(inputCorreo.value, "correo")) return false;
    if (!validarCampoVacio(fechaNac, "fechaNac")) return false;

    if (dirImagen) {
        const extension = dirImagen.split('.').pop().toLowerCase();
        if (!["jpg", "jpeg", "png"].includes(extension)) {
            alert("La imagen debe ser JPG o PNG");
            return false;
        }
    }

    let usrSelec = null;
    for (const usr of tipoUsuario) {
        if (usr.checked) {
            usrSelec = usr.value;
            break;
        }
    }

    if (usrSelec) {
        if (usrSelec.toString() === "PROPONENTE") {
            if (!validarCampoVacio(direccion, "direccion")) return false;
        }
    } else {
        window.alert("Seleccione un tipo de usuario");
        return false;
    }

    return true;
}

function mostrarCampos(opt) {
    const todoDireccion = document.getElementById("todoDireccion");
    const todoBiografia = document.getElementById("todoBiografia");
    const todoLink = document.getElementById("todoLink");
    const direccion = document.getElementById("direccion");

    if (opt === '1') {
        direccion.required = true;
        todoDireccion.style.display = "block";
        todoBiografia.style.display = "block";
        todoLink.style.display = "block";
    } else if (opt === '2') {
        direccion.required = false;
        todoDireccion.style.display = "none";
        todoBiografia.style.display = "none";
        todoLink.style.display = "none";
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const inputNick = document.getElementById("nick");
    const msgNick = document.getElementById("mensajeNick");
    const inputEmail = document.getElementById("correo");
    const msgEmail = document.getElementById("mensajeCorreo");

    function verificarDisponibilidad(tipo, valor, elementoMensaje, inputElement) {
        if (!valor) {
            elementoMensaje.textContent = "";
            inputElement.classList.remove("is-invalid", "is-valid");
            return;
        }

        fetch(`verificarUsuario?tipo=${tipo}&valor=${encodeURIComponent(valor)}`)
            .then(response => response.text())
            .then(existe => {
                if (existe.trim() === "true") {
                    elementoMensaje.textContent = "El " + (tipo === 'nick' ? 'nickname' : 'correo') + " ya está en uso.";
                    elementoMensaje.style.color = "red";
                    inputElement.classList.add("is-invalid");
                    inputElement.classList.remove("is-valid");
                } else {

                    elementoMensaje.textContent = "Disponible";
                    elementoMensaje.style.color = "green";
                    inputElement.classList.remove("is-invalid");
                    inputElement.classList.add("is-valid");
                }
            })
            .catch(error => console.error("Error verificando:", error));
    }

    inputNick.addEventListener("input", function () {
        verificarDisponibilidad("nick", this.value, msgNick, inputNick);
    });

    inputEmail.addEventListener("input", function () {
        verificarDisponibilidad("email", this.value, msgEmail, inputEmail);
    });
});