<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="../fragments/header.jsp" />
</head>
<body>
<div class="container my-5 content">
<h3 class="fw-bold mb-4">Редактировать симптом</h3>

<form action="${pageContext.request.contextPath}/symptoms/edit/${symptom.id}" method="post" class="w-100">
    <input type="hidden" name="id" value="${symptom.id}"/>

    <div class="mb-3">
        <label class="form-label fw-semibold">Название <span class="text-danger">*</span></label>
        <input type="text" class="form-control" name="name" value="${symptom.name}" required>
    </div>

    <button type="submit" class="btn btn-primary px-4">Обновить</button>
    <a href="${pageContext.request.contextPath}/symptoms" class="btn btn-secondary px-4">Отмена</a>
</form>
</div>
</body>
</html>
