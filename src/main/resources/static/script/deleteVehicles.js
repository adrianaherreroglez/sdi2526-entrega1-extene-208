$(document).ready(function () {
    $("#deleteSelectedVehicles").click(function () {
        let selectedVehicleIds = [];

        $(".vehicle-checkbox:checked").each(function () {
            selectedVehicleIds.push($(this).val());
        });

        $.ajax({
            url: "/vehicle/delete",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(selectedVehicleIds),
            success: function () {
                location.reload(); // Recargar la lista de vehículos
            },

        });
    });
});
