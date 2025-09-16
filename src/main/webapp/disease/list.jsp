<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <%-- Подключение общего header --%>
    <jsp:include page="/fragments/header.jsp" />
</head>
<body>

<div class="container my-5 content">
    <h3 class="fw-bold mb-4">Список болезней</h3>

    <c:forEach var="disease" items="${diseasesPage}">
        <div class="card p-4 mb-3 shadow-sm d-flex flex-row justify-content-between align-items-center">
            <h5 class="fw-bold mb-0">${disease.name}</h5>

            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/diseases/${disease.id}/edit"
                   class="btn btn-edit px-4">Редактировать</a>

                <form action="${pageContext.request.contextPath}/diseases/${disease.id}/delete"
                      method="post" class="d-inline">
                    <button type="submit" class="btn btn-delete px-4">Удалить</button>
                </form>
            </div>
        </div>
    </c:forEach>

    <c:if test="${totalPages > 1}">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center mt-4">
                <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="?page=${currentPage - 1}&size=${pageSize}">‹</a>
                </li>

                <c:forEach var="i" begin="0" end="${totalPages - 1}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="?page=${i}&size=${pageSize}">${i + 1}</a>
                    </li>
                </c:forEach>

                <li class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="?page=${currentPage + 1}&size=${pageSize}">›</a>
                </li>
            </ul>
        </nav>
    </c:if>
</div>

<jsp:include page="/fragments/footer.jsp" />

</body>
</html>
