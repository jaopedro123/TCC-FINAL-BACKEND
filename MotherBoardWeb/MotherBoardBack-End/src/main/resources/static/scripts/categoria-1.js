$(document).ready(function() {
    var $filterMenu = $('#filterMenu');
    var $overlay = $('.overlay');
    var $productContainer = $('#promotionBanner'); // Atualizado para usar ID
    var $footer = $('#footerdocate');
    var isMobile = $(window).width() <= 767;

    function checkScroll() {
        var scrollTop = $(window).scrollTop();
        var productContainerOffset = $productContainer.offset().top;
        var productContainerHeight = $productContainer.outerHeight();
        var filterMenuHeight = $filterMenu.outerHeight();

        if (!isMobile) {
            if (scrollTop > productContainerOffset) {
                if (scrollTop + filterMenuHeight > productContainerOffset + productContainerHeight) {
                    $filterMenu.css({
                        position: 'absolute',
                        top: productContainerOffset + productContainerHeight - filterMenuHeight + 'px'
                    });
                } else {
                    $filterMenu.css({
                        position: 'fixed',
                        top: '0px'
                    });
                }
            } else {
                $filterMenu.css({
                    position: 'absolute',
                    top: productContainerOffset + 'px'
                });
            }
        }
    }

    function resetFilterMenuForDesktop() {
        $filterMenu.css({
            position: '',
            top: '',
            left: '',
            width: '',
            height: '',
            overflowY: '',
            display: '' // Garante que o menu seja exibido
        }).removeClass('show');
        $overlay.removeClass('active').hide();
    }

    $(window).on('scroll', function() {
        isMobile = $(window).width() <= 767;
        if (!isMobile) {
            checkScroll();
        }
    });

    $(window).on('resize', function() {
        isMobile = $(window).width() <= 767;
        if (isMobile) {
            $filterMenu.css({
                position: 'fixed',
                left: '-250px',
                top: '0',
                width: '250px',
                height: '100%',
                overflowY: 'auto'
            });
            $overlay.hide();
        } else {
            resetFilterMenuForDesktop();
            checkScroll();
        }
    });

    $('#toggleFilters').click(function() {
        $filterMenu.toggleClass('show');
        $overlay.toggleClass('active');
        if ($filterMenu.hasClass('show')) {
            $filterMenu.css({
                left: '0',
                top: '0',
                overflowY: 'auto'
            });
            $overlay.show();
        } else {
            $filterMenu.css({
                left: '-250px',
            });
            $overlay.hide();
        }
    });

    $('#closeFilters, .overlay').click(function() {
        $filterMenu.removeClass('show');
        $overlay.removeClass('active').hide();
        $filterMenu.css({
            left: '-250px',
            top: '0',
            overflowY: 'auto'
        });
    });


    $('.subfilters').hide();

    // Função para exibir ou ocultar subfiltros ao clicar em um item de categoria
    $('.list-group-item').click(function(e) {
        e.preventDefault();
        
        var category = $(this).data('category');
        var $subfilters = $('#subfilters-' + category);

        // Esconde todos os subfiltros, exceto o clicado
        $('.subfilters').not($subfilters).slideUp();
        
        // Alterna o subfiltro atual
        if ($subfilters.length) {
            $subfilters.slideToggle();
        }

        // Se for um link de categoria principal, aplicar a filtragem de categoria
        if (category) {
            $('.produtocate').hide();
            $('.produtocate[data-category="' + category + '"]').show();
            if (category === 'all') {
                $('.produtocate').show();
            }
        }
    });

    // Função para aplicar a filtragem de subcategoria
    $('.subfilters .list-group-item').click(function(e) {
        e.preventDefault();
        
        var subcategory = $(this).data('subcategory');

        // Esconder todos os produtos, exceto os que pertencem à subcategoria selecionada
        $('.produtocate').hide();
        $('.produtocate[data-subcategory="' + subcategory + '"]').show();
    });

    function formatPrice(value) {
        // Remove tudo que não é número
        var cleanValue = value.replace(/\D/g, '');

        // Limita a 6 dígitos
        if (cleanValue.length > 6) {
            cleanValue = cleanValue.substring(0, 6);
        }

        // Adiciona a vírgula como separador de milhares
        cleanValue = cleanValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');

        return cleanValue;
    }

    $('#minPrice, #maxPrice').on('input', function() {
        // Atualiza o valor formatado
        this.value = formatPrice(this.value);
    });

    // Função de filtragem por preço
    $('#filterPrice').click(function() {
        var minPrice = $('#minPrice').val().replace(/,/g, '');
        var maxPrice = $('#maxPrice').val().replace(/,/g, '');

        minPrice = parseFloat(minPrice) || 0;
        maxPrice = parseFloat(maxPrice) || Infinity;

        $('.produtocate').each(function() {
            var price = parseFloat($(this).data('price'));
            if (!isNaN(price) && (price >= minPrice && price <= maxPrice)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

    checkScroll(); // Verifica a posição inicial
    
        function toggleClasses() {
            const myRow = document.getElementById('myRow');

            // Verifica se o elemento existe
            if (myRow) {
                if (window.innerWidth > 1800) { // Maior que 1800px
                    myRow.classList.remove('row-cols-md-3');
                    myRow.classList.add('row-cols-md-4');
                } else { // 1800px ou menor
                    myRow.classList.remove('row-cols-md-4');
                    myRow.classList.add('row-cols-md-3');
                }
            } else {
                console.error("Elemento com ID 'myRow' não encontrado.");
            }
        }

        // Executa a função ao carregar a página
        toggleClasses();

        // Adiciona um event listener para redimensionamento da janela
        window.addEventListener('resize', toggleClasses);
  
});
