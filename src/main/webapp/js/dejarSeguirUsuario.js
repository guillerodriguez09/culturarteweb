document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formDejarSeguir");

    form.addEventListener("submit", (e) => {
        const select = document.getElementById("usuarioSeguido");
        if (!select.value) {
            e.preventDefault();
            alert("Debe seleccionar un usuario para dejar de seguir.");
        }
    });
});
