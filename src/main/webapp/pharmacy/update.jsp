<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="/fragments/header.jsp"/>
</head>
<body>
<div class="container my-5 content">
    <h3 class="fw-bold mb-4">Добавить лекарство</h3>

    <form action="${pageContext.request.contextPath}/pharmacies/edit/${medication.pharmacyId}"
          method="post"
          enctype="multipart/form-data"
          class="w-75">
        <input type="hidden" name="id" value="${medication.id}">

        <div class="mb-3">
            <label class="form-label fw-semibold">Название <span class="text-danger">*</span></label>
            <input type="text" name="name" value="${medication.name}" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Описание <span class="text-danger">*</span></label>
            <textarea name="description" class="form-control" rows="3"
                      required>${medication.description}</textarea>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Количество, шт <span class="text-danger">*</span></label>
            <input type="number" name="quantity" value="${medication.quantity}" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Дата покупки <span class="text-danger">*</span></label>
            <input type="date" name="purchaseDate" value="${medication.purchaseDate}" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Срок годности до <span class="text-danger">*</span></label>
            <input type="date" name="expirationDate" value="${medication.expirationDate}" class="form-control" required>
        </div>

        <div class="mb-4">
            <label class="form-label fw-semibold">Фото</label>

            <div class="mb-2">
                <c:if test="${not empty medication.image}">
                    <img id="currentImage"
                         src="${pageContext.request.contextPath}/uploads${medication.image}"
                         alt="Фото лекарства"
                         class="img-thumbnail"
                         style="max-width: 200px; max-height: 200px;">
                </c:if>
            </div>

            <input class="form-control" type="file" name="image" id="imageInput">
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", () => {
                const input = document.getElementById("imageInput");
                const currentImage = document.getElementById("currentImage");

                input.addEventListener("change", () => {
                    const file = input.files[0];
                    if (file) {
                        const reader = new FileReader();
                        reader.onload = e => {
                            currentImage.src = e.target.result;
                            currentImage.style.display = "block";
                        };
                        reader.readAsDataURL(file);
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
                            <input type="checkbox" name="diseaseIds"
                                   value="${disease.id}"
                                    <c:forEach var="sid" items="${medication.diseaseIds}">
                                           <c:if test="${sid == disease.id}">checked</c:if>
                                    </c:forEach>>

                        </td>
                        <td>${disease.name}</td>
                        <td>${disease.description}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <button type="submit" class="btn btn-save px-4">Обновить</button>
        <a href="${pageContext.request.contextPath}/pharmacies" class="btn btn-cancel px-4">Отмена</a>
    </form>
</div>

</body>
</html>
