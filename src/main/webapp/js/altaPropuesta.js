// validaciones específicas para Alta Propuesta

function validarCampoVacio(valor, nombreCampo) {
    if (!valor || valor.trim() === "") {
        alert(`El campo "${nombreCampo}" es obligatorio.`);
        return false;
    }
    return true;
}

function validarAltaPropuesta() {
    const proponente = document.getElementById("proponenteNick").value;
    const categoria = document.getElementById("categoriaNombre").value;
    const titulo = document.getElementById("titulo").value.trim();
    const descripcion = document.getElementById("descripcion").value.trim();
    const lugar = document.getElementById("lugar").value.trim();
    const fecha = document.getElementById("fecha").value;
    const precioEntrada = document.getElementById("precioEntrada").value;
    const montoAReunir = document.getElementById("montoAReunir").value;
    const retornos = document.querySelectorAll('input[name="retornos"]:checked');
    const imagen = document.getElementById("imagen").value;

    // Validaciones básicas
    if (!validarCampoVacio(titulo, "Título")) return false;
    if (!validarCampoVacio(descripcion, "Descripción")) return false;
    if (!validarCampoVacio(lugar, "Lugar")) return false;
    if (!validarCampoVacio(fecha, "Fecha")) return false;
    if (!validarCampoVacio(precioEntrada, "Precio de Entrada")) return false;
    if (!validarCampoVacio(montoAReunir, "Monto a Reunir")) return false;


    const hoy = new Date().toISOString().split("T")[0];
    if (fecha < hoy) {
        alert("La fecha debe ser posterior a la actual.");
        return false;
    }


    if (isNaN(precioEntrada) || precioEntrada <= 0) {
        alert("El precio de entrada debe ser un número positivo.");
        return false;
    }
    if (isNaN(montoAReunir) || montoAReunir <= 0) {
        alert("El monto a reunir debe ser un número positivo.");
        return false;
    }


    if (retornos.length === 0) {
        alert("Debe seleccionar al menos un tipo de retorno.");
        return false;
    }


    if (imagen) {
        const extension = imagen.split('.').pop().toLowerCase();
        if (!["jpg", "jpeg", "png"].includes(extension)) {
            alert("La imagen debe ser JPG o PNG.");
            return false;
        }
    }

    return true;
}

