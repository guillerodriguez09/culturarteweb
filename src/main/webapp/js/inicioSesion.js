function validarCampoVacio(valor, nombreCampo) {
    if (!valor || valor.trim() === "") {
        alert(`El campo "${nombreCampo}" es obligatorio.`);
        return false;
    }
    return true;
}

function validarInicioSesion(){
    const nickOMail = document.getElementById(nickOMail).value.trim();
    const contrasenia = document.getElementById(contrasenia).value.trim();

    if(!validarCampoVacio(nickOMail, "Usuario o Correo")) return false;
    if(!validarCampoVacio(contrasenia, "Contraseña"))return false;

    return true;

}