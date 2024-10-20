var extraImagesCount = 0;

$(document).ready(function () {

    $("#fileImage").change(function () {
        if (!checkFileSize(this)) {
            return;
        }

        addCerto(this)
        showImageThumbnail(this);
    });

});

function addCerto(fileInput) {
    fileInput.classList.remove('is-invalid');
    fileInput.classList.add('is-valid');
}

function addInvalido(fileInput) {
    fileInput.classList.remove('is-valid');
    fileInput.classList.add('is-invalid');
}

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}

function checkFileSize(fileInput) {
    const fileSize = fileInput.files[0].size;

    if (fileSize > 1 * 1024 * 1024) {
        showModalDialog("Desculpe...", "Você só pode escolher imagens abaixo de 1MB! ");
        addInvalido(fileInput);
        fileInput.nextElementSibling.textContent = 'A imagem deve ter no máximo 1MB.';
        showImageThumbnail(fileImage)

        return false;
    }
    else {
        fileInput.setCustomValidity("");

        addCerto(fileInput)

        return true;
    }
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal('show');
}

// Adiciona um event listener para cada imagem com a classe 'clickable-image'
document.querySelectorAll('.clickable-image').forEach((img) => {
    img.addEventListener('click', function () {
        // Procura o próximo input 'file' associado à imagem clicada e ativa o clique
        const fileInput = this.parentElement.nextElementSibling;
        if (fileInput && fileInput.type === 'file') {
            fileInput.click();
        }
    });
});