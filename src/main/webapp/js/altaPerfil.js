function validarAltaPerfil(){

    const nickname = document.getElementById("nick").value;
    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const contrasenia = document.getElementById("contrasenia").value;
    const correo = document.getElementById("correo").value;
    const fechaNac = document.getElementById("fechaNac").value;
    const dirImagen = document.getElementById("dirImagen").value;

    const direccion = document.getElementById("direccion").value;
    const tipoUsuario = document.querySelectorAll('input[name="tipoUsuario"]');
    let usrSelec = null;

    if(!validarCampoVacio(nickname, "nick")) return false;
    if(!validarCampoVacio(nombre, "nombre")) return false;
    if(!validarCampoVacio(apellido, "apellido")) return false;
    if(!validarCampoVacio(contrasenia, "contrasenia")) return false;
    if(!validarCampoVacio(correo, "correo")) return false;
    if(!validarCampoVacio(fechaNac, "fechaNac")) return false;

    if(dirImagen){
        const extension = dirImagen.split('.').pop().toLowerCase();
        if(!["jpg", "jpeg", "png"].includes(extension)){
            alert("La imagen debe ser JPG o PNG")
            return false
        }
    }

    for(const usr of tipoUsuario){
        if(usr.checked){
            usrSelec = usr.value;
            break;
        }
    }

    if(usrSelec) {
        if (usrSelec.toString() === "PROPONENTE") {
            if(!validarCampoVacio(direccion, "direccion")) return false;
        }
    }else{
        window.alert("Seleccione un tipo de usuario");
    }



    return true

}

function mostrarCampos(opt){

    const todoDireccion = document.getElementById("todoDireccion");
    const todoBiografia = document.getElementById("todoBiografia");
    const todoLink = document.getElementById("todoLink");
    const direccion = document.getElementById("direccion");

    if(opt === '1'){
        direccion.required = true;
        todoDireccion.style.display = "block";
        todoBiografia.style.display = "block";
        todoLink.style.display = "block";
    } else if(opt === '2'){
        direccion.required = false;
        todoDireccion.style.display = "none";
        todoBiografia.style.display = "none";
        todoLink.style.display = "none";
    }

}