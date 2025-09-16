<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="../fragments/header.jsp" />
</head>
<body>
<div class="container my-5 content">
<h3 class="fw-bold mb-4">Добавить болезнь</h3>

<form action="${pageContext.request.contextPath}/diseases/add" method="post" class="w-100">
    <div class="mb-3">
        <label class="form-label fw-semibold">Название <span class="text-danger">*</span></label>
        <input type="text" class="form-control" name="name" required>
    </div>

    <div class="mb-3">
        <label class="form-label fw-semibold">Описание <span class="text-danger">*</span></label>
        <input type="text" class="form-control" name="description" required>
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
                        <input type="checkbox" name="symptomIds" value="${symptom.id}">
                    </td>
                    <td>${symptom.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <button type="submit" class="btn btn-primary px-4">Создать</button>
    <a href="${pageContext.request.contextPath}/diseases" class="btn btn-secondary">Отмена</a>
</form>
</div>
</body>
</html>
