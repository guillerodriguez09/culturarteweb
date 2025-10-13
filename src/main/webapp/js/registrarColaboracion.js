// Valida formulario
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

// Carga info de la propuesta seleccionada
function cargarPropuesta() {
    const select = document.getElementById("propuestaTitulo");
    const opcion = select.options[select.selectedIndex];

    if (!opcion.value) {
        document.getElementById("infoPropuesta").style.display = "none";
        return;
    }

    document.getElementById("descripcion").textContent = opcion.dataset.descripcion;
    document.getElementById("lugar").textContent = opcion.dataset.lugar;
    document.getElementById("fecha").textContent = opcion.dataset.fecha;
    document.getElementById("precioEntrada").textContent = opcion.dataset.precioentrada;
    document.getElementById("montoAReunir").textContent = opcion.dataset.montoareunir;
    document.getElementById("estado").textContent = opcion.dataset.estado;
    document.getElementById("montoRecaudado").textContent = opcion.dataset.montorecaudado;

    document.getElementById("infoPropuesta").style.display = "block";
}
