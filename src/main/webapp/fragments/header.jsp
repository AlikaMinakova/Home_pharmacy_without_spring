<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title != null ? title : 'Аптека.дома'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm py-3">
    <div class="container">

        <div class="d-none d-lg-flex gap-3">

            <div class="dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="medDropdown"
                   role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Лекарства
                </a>
                <ul class="dropdown-menu" aria-labelledby="medDropdown">
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/pharmacies">Список лекарств</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/pharmacies/add">Добавить лекарство</a></li>
                </ul>
            </div>

            <div class="dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="disDropdown"
                   role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Болезни
                </a>
                <ul class="dropdown-menu" aria-labelledby="disDropdown">
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/diseases">Список болезней</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/diseases/add">Добавить болезнь</a></li>
                </ul>
            </div>

            <div class="dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="symDropdown"
                   role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Симптомы
                </a>
                <ul class="dropdown-menu" aria-labelledby="symDropdown">
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/symptoms">Список симптомов</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/symptoms/add">Добавить симптом</a></li>
                </ul>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/pharmacies" method="get" class="d-flex">
            <input type="text" name="keyword" value="${keyword}" class="form-control me-2"
                   placeholder="Поиск по названию...">
            <button type="submit" class="btn btn-outline-primary">Найти</button>
        </form>

        <a class="navbar-brand fw-bold text-primary" href="${pageContext.request.contextPath}/pharmacies">Аптека.дома</a>
    </div>
</nav>

<style>
    html, body {
        height: 100%;
        margin: 0;
    }

    body {
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }

    .content {
        flex: 1;
    }

    .footer {
        margin-top: auto;
    }

    .card-img-fixed {
        width: 100%;
        height: 200px;
        object-fit: contain;
        background-color: #fff;
        padding: 10px;
        border-top-left-radius: 0.5rem;
        border-top-right-radius: 0.5rem;
    }

    .btn-edit {
        background-color: #20e2b4;
        color: white;
        border: none;
        border-radius: 50px;
        font-weight: 500;
    }

    .btn-delete {
        background-color: #dc3545;
        color: white;
        border: none;
        border-radius: 50px;
        font-weight: 500;
    }

    .btn-save {
        background-color: #0b5ed7;
        color: white;
        border: none;
        border-radius: 50px;
        font-weight: 500;
    }

    .btn-cancel {
        background-color: #C0BFC1;
        color: white;
        border: none;
        border-radius: 50px;
        font-weight: 500;
    }

</style>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
