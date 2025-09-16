<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="../fragments/header.jsp"/>
</head>
<body>
<div class="container my-5 content">

    <h3 class="fw-bold mb-4">${medication.name}</h3>

    <div class="row">

        <div class="col-md-4 text-center">
            <c:if test="${not empty medication.image}">
                <img src="${pageContext.request.contextPath}/uploads${medication.image}"
                     class="img-thumbnail"
                     alt="Фото лекарства">
            </c:if>
        </div>

        <div class="col-md-6">
            <div class="d-flex justify-content-end gap-2 mb-3">

                <a href="${pageContext.request.contextPath}/pharmacies/edit/${medication.pharmacyId}"
                   class="btn btn-edit px-4">Редактировать</a>

                <form action="${pageContext.request.contextPath}/pharmacies/delete/${medication.id}"
                      method="post" class="d-inline">
                    <button type="submit" class="btn btn-delete px-4">Удалить</button>
                </form>
            </div>

            <div class="card p-3 mb-4 shadow-sm">
                <h6 class="fw-bold">Основное</h6>
                <ul class="list-unstyled small">
                    <li>Описание: ${medication.description}</li>
                    <li>Осталось: ${medication.quantity} шт</li>
                    <li>Дата покупки: ${medication.purchaseDate}</li>
                    <li>Срок годности: ${medication.expirationDate}</li>
                </ul>
            </div>

            <div class="card p-3 shadow-sm">
                <h6 class="fw-bold">От чего</h6>
                <table class="table table-sm">
                    <thead>
                    <tr>
                        <th>Болезни</th>
                        <th>Симптомы</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="disease" items="${medication.diseaseResponses}">
                        <tr>
                            <td>${disease.name}</td>
                            <td>
                                <c:forEach var="symptom" items="${disease.symptomsResponses}" varStatus="stat">
                                    ${symptom.name}<c:if test="${!stat.last}">, </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
