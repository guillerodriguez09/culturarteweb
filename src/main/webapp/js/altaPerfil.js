function validarAltaPerfil(){

    const nickname = document.getElementById("nick").value;
    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const contrasenia = document.getElementById("contrasenia").value;
    const correo = document.getElementById("correo").value;
    const fechaNac = document.getElementById("fechaNac").value;
    const dirImagen = document.getElementById("dirImagen").value;

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

    return true

}