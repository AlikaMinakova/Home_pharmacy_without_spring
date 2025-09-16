<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="/fragments/header.jsp" />
</head>
<body>

<div class="container my-5 content">

<h3 class="fw-bold mb-4">Редактировать болезнь</h3>

<form action="${pageContext.request.contextPath}/diseases/edit/${disease.id}"
      method="post" class="w-100">

    <input type="hidden" name="id" value="${disease.id}"/>

    <div class="mb-3">
        <label class="form-label fw-semibold">Название <span class="text-danger">*</span></label>
        <input type="text" class="form-control" name="name" value="${disease.name}" required>
    </div>

    <div class="mb-3">
        <label class="form-label fw-semibold">Описание <span class="text-danger">*</span></label>
        <input type="text" class="form-control" name="description" value="${disease.description}" required>
    </div>

    <div class="mb-4">
        <label class="form-label fw-semibold">Симптомы</label>
        <table class="table table-bordered table-hover">
            <thead class="table-light">
            <tr>
                <th style="width: 50px;">Выбрать</th>
                <th>Название</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="symptom" items="${symptoms}">
                <tr>
                    <td class="text-center">
                        <input type="checkbox" name="symptomIds" value="${symptom.id}"
                        <c:forEach var="sid" items="${disease.symptomIds}">
                               <c:if test="${sid == symptom.id}">checked</c:if>
                        </c:forEach>>
                    </td>
                    <td>${symptom.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <button type="submit" class="btn btn-primary px-4">Обновить</button>
    <a href="${pageContext.request.contextPath}/diseases" class="btn btn-secondary px-4">Отмена</a>
</form>
</div>
</body>
</html>
