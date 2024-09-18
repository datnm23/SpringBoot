$(document).ready(function () {

    resetForm();
    $(document).on("change", "tbody input[type='checkbox']", function () {
        updateButtonStates();
    });

    $("#save-btn").click(function () {
        const name = $("#name").val();
        const dob = $("#dob").val();
        const phone = $("#phone").val();
        const hometown = $("#home-town").val();

        // validate dữ liệu
        const error = validateData(name, dob, phone, hometown);
        if (error) {
            return;
        }

        const dobDate = new Date(dob);
        const formattedDob = dobDate.getDate() + '/' + (dobDate.getMonth() + 1) + "/" + dobDate.getFullYear();

        const checkedRow = $("tbody input[type='checkbox']:checked").closest("tr");
        if (checkedRow.length) {
            updateStudentRow(checkedRow, name, formattedDob, phone, hometown);
        } else {
            addNewStudentRow(name, formattedDob, phone, hometown);
        }

        $("#name, #dob, #phone, #home-town").val("");
        $("tbody input[type='checkbox']").prop("checked", false);
        updateButtonStates();
        resetForm();
    });

    $("#edit-btn").click(function () {
        const checkedRows = $("tbody input[type='checkbox']:checked");
        if (checkedRows.length > 1) {
            alert("Bạn chỉ được sửa thông tin của 1 sinh viên");
            return;
        }
        if (checkedRows.length === 1) {
            const row = checkedRows.closest("tr");
            fillFormWithRowData(row);
        }
    });

    $("#delete-btn").click(function () {
        const checkedRows = $("tbody input[type='checkbox']:checked");
        if (checkedRows.length === 0) {
            alert("Vui lòng chọn ít nhất một sinh viên để xóa");
            return;
        }
        
        if (confirm("Bạn có chắc chắn muốn xóa sinh viên đang chọn?")) {
            checkedRows.closest("tr").remove();
            updateButtonStates();
        }
    });

    function fillFormWithRowData(row) {
        $("#name").val(row.find("td:eq(1)").text());
        $("#dob").val(row.find("td:eq(2)").text().split('/').reverse().join('-'));
        $("#phone").val(row.find("td:eq(3)").text());
        $("#home-town").val(row.find("td:eq(4)").text());
    }

    function updateButtonStates() {
        const checkedRows = $("tbody input[type='checkbox']:checked").length;
        $("#delete-btn").prop("disabled", checkedRows === 0);
        $("#edit-btn").prop("disabled", checkedRows !== 1);
    }

    function updateStudentRow(row, name, dob, phone, hometown) {
        row.find("td:eq(1)").text(name);
        row.find("td:eq(2)").text(dob);
        row.find("td:eq(3)").text(phone);
        row.find("td:eq(4)").text(hometown);
    }

    function addNewStudentRow(name, dob, phone, hometown) {
        const newRow = `
            <tr>
                <td class="checkbox-col"><input type="checkbox" /></td>
                <td>${name}</td>
                <td>${dob}</td>
                <td>${phone}</td>
                <td>${hometown}</td>
            </tr>
        `;
        $("tbody").append(newRow);
    }

    function validateData(name, dob, phone, hometown) {
        let error = false;
        if (!name || name?.trim() === "") {
            $(".form-group:nth-child(1) .error").removeClass("d-none");
            $(".form-group:nth-child(1) .error").text("Tên bắt buộc nhập")
            error = true;
        }
        if (name?.trim()?.length > 100) {
            $(".form-group:nth-child(1) .error").removeClass("d-none");
            $(".form-group:nth-child(1) .error").text("Tên không được quá 100 ký tự")
            error = true;
        }
        if (!dob || dob?.trim() === "") {
            $(".form-group:nth-child(2) .error").removeClass("d-none");
            error = true;
        }
        const phoneRegex = /^0\d{9}$/;
        if (!phone || phone?.trim() === "" || !phoneRegex.test(phone?.trim())) {
            $(".form-group:nth-child(3) .error").removeClass("d-none");
            error = true;
        }
        if (!hometown || hometown?.trim() === "") {
            $(".form-group:nth-child(4) .error").removeClass("d-none");
            error = true;
        }
        return error;
    }

    function resetForm() {
        $(".form-group:nth-child(1) .error").addClass("d-none");
        $(".form-group:nth-child(2) .error").addClass("d-none");
        $(".form-group:nth-child(3) .error").addClass("d-none");
        $(".form-group:nth-child(4) .error").addClass("d-none");
    }

    function fillFormWithRowData(row) {
        $("#name").val(row.find("td:eq(1)").text());
        $("#dob").val(row.find("td:eq(2)").text().split('/').reverse().join('-'));
        $("#phone").val(row.find("td:eq(3)").text());
        $("#home-town").val(row.find("td:eq(4)").text());
    }
    updateButtonStates();
});