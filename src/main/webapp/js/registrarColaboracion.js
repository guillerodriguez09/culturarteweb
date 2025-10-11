function validarRegistrarColaboracion() {
    const propuesta = document.getElementById("propuestaTitulo").value;
    const retorno = document.getElementById("tipoRetorno").value;
    const monto = document.getElementById("monto").value;

    if (!propuesta) {
        alert("Debe seleccionar una propuesta.");
        return false;
    }

    if (!retorno) {
        alert("Debe seleccionar un tipo de retorno.");
        return false;
    }

    if (!monto || isNaN(monto) || monto <= 0) {
        alert("El monto debe ser un número positivo.");
        return false;
    }

    return true;
}
