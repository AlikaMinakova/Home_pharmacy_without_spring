<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="../fragments/header.jsp"/>
</head>
<body>
<div class="container my-5 content">
    <h3 class="fw-bold mb-4">Добавить лекарство</h3>

    <form action="${pageContext.request.contextPath}/pharmacies/add"
          method="post"
          enctype="multipart/form-data"
          class="w-75">

        <div class="mb-3">
            <label class="form-label fw-semibold">Название <span class="text-danger">*</span></label>
            <input type="text" name="name" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Описание <span class="text-danger">*</span></label>
            <textarea name="description" class="form-control" rows="3" required></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Количество, шт <span class="text-danger">*</span></label>
            <input type="number" name="quantity" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Дата покупки <span class="text-danger">*</span></label>
            <input type="date" name="purchaseDate" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Срок годности до <span class="text-danger">*</span></label>
            <input type="date" name="expirationDate" class="form-control" required>
        </div>

        <div class="mb-4">
            <label class="form-label fw-semibold">Фото</label>

            <div class="mb-2">
                <img id="previewImage"
                     src="#"
                     alt="Предпросмотр"
                     class="img-thumbnail"
                     style="max-width: 200px; max-height: 200px; display: none;">
            </div>

            <input class="form-control" type="file" name="image" id="imageInput">
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", () => {
                const input = document.getElementById("imageInput");
                const preview = document.getElementById("previewImage");

                input.addEventListener("change", () => {
                    const file = input.files[0];
                    if (file) {
                        const reader = new FileReader();
                        reader.onload = e => {
                            preview.src = e.target.result;
                            preview.style.display = "block";
                        };
                        reader.readAsDataURL(file);
                    } else {
                        preview.src = "#";
                        preview.style.display = "none";
                    }
                });
            });
        </script>

        <div class="mb-4">
            <label class="form-label fw-semibold">Болезни</label>
            <table class="table table-bordered table-hover">
                <thead class="table-light">
                <tr>
                    <th style="width: 50px;">Выбрать</th>
                    <th>Название</th>
                    <th>Описание</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="disease" items="${diseases}">
                    <tr>
                        <td class="text-center">
                            <input type="checkbox" name="diseaseIds" value="${disease.id}">
                        </td>
                        <td>${disease.name}</td>
                        <td>${disease.description}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <button type="submit" class="btn btn-save px-4">Создать</button>
        <p></p>
    </form>
</div>
</body>
</html>
