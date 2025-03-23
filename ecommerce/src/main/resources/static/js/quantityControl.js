$(document).ready(function () {
    $(".minusButton, .plusButton").on("click", function (evt) {
        evt.preventDefault();
        let productId = $(this).attr("id");
        let qtyInput = $("#quantity" + productId);
        let newQty = parseInt(qtyInput.val()) + ($(this).hasClass("plusButton") ? 1 : -1);
        if (newQty > 0 && newQty <= 10) {
            qtyInput.val(newQty);
        }
    });
});
