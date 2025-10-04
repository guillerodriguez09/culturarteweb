// Funciones de validación reutilizables

function validarCampoVacio(valor, nombreCampo) {
    if (!valor || valor.trim() === "") {
        alert(`Debe ingresar un valor en ${nombreCampo}.`);
        return false;
    }
    return true;
}


