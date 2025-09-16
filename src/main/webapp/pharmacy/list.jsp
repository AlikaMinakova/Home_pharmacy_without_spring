<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <jsp:include page="/fragments/header.jsp"/>
</head>
<body>
<div class="container my-5 content">
    <h3 class="fw-bold mb-4">Аптечка</h3>

    <!-- сортировка -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <span class="fw-semibold">СОРТИРОВАТЬ:</span>
        <form action="${pageContext.request.contextPath}/pharmacies" method="get" class="ms-3">
            <select name="sort" class="form-select w-auto" onchange="this.form.submit()">
                <option value="purchase_date" ${sort == 'purchase_date' ? 'selected' : ''}>По дате покупки</option>
                <option value="expiration_date" ${sort == 'expiration_date' ? 'selected' : ''}>По сроку годности</option>
                <option value="quantity" ${sort == 'quantity' ? 'selected' : ''}>По количеству</option>
            </select>
            <input type="hidden" name="keyword" value="${keyword}">
        </form>
    </div>

    <!-- список -->
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4 mb-5">
        <c:forEach var="pharmacy" items="${pharmacyPage.all}">
            <div class="col">
                <div class="card h-100 shadow-sm">
                    <c:if test="${not empty pharmacy.medication.image}">
                        <img src="${pageContext.request.contextPath}/uploads${pharmacy.medication.image}"
                             class="card-img-fixed" alt="Фото лекарства">
                    </c:if>
                    <div class="card-body">
                        <p class="text-success small fw-semibold">Есть в аптечке</p>
                        <h6 class="fw-bold">
                            <a href="${pageContext.request.contextPath}/pharmacies/detail/${pharmacy.id}"
                               class="text-decoration-none text-dark">
                                    ${pharmacy.medication.name}
                            </a>
                        </h6>
                        <ul class="list-unstyled small text-muted">
                            <li>Дата покупки: ${pharmacy.purchaseDate}</li>
                            <li>Срок годности: ${pharmacy.expirationDate}</li>
                            <li>Количество: ${pharmacy.quantity} шт</li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${totalPages > 1}">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center mt-4">

                <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="?page=${currentPage - 1}&size=${pageSize}&sort=${sort}&keyword=${keyword}">‹</a>
                </li>

                <c:set var="start" value="${currentPage - 2 lt 0 ? 0 : currentPage - 2}" />
                <c:set var="end" value="${currentPage + 2 gt totalPages - 1 ? totalPages - 1 : currentPage + 2}" />

                <c:if test="${start > 0}">
                    <li class="page-item">
                        <a class="page-link"
                           href="?page=0&size=${pageSize}&sort=${sort}&keyword=${keyword}">1</a>
                    </li>
                    <c:if test="${start > 1}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>
                </c:if>

                <c:forEach var="i" begin="${start}" end="${end}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link"
                           href="?page=${i}&size=${pageSize}&sort=${sort}&keyword=${keyword}">${i + 1}</a>
                    </li>
                </c:forEach>

                <c:if test="${end < totalPages - 1}">
                    <c:if test="${end < totalPages - 2}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>
                    <li class="page-item">
                        <a class="page-link"
                           href="?page=${totalPages - 1}&size=${pageSize}&sort=${sort}&keyword=${keyword}">
                                ${totalPages}
                        </a>
                    </li>
                </c:if>

                <li class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="?page=${currentPage + 1}&size=${pageSize}&sort=${sort}&keyword=${keyword}">›</a>
                </li>
            </ul>
        </nav>
    </c:if>

    <!-- Купили недавно -->
    <h4 class="fw-bold mb-4">Купили недавно</h4>
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4 mb-5">
        <c:forEach var="pharmacy" items="${pharmacyPage.recentlyBought}">
            <div class="col">
                <div class="card h-100 shadow-sm">
                    <c:if test="${not empty pharmacy.medication.image}">
                        <img src="${pageContext.request.contextPath}/uploads${pharmacy.medication.image}"
                             class="card-img-fixed" alt="Фото лекарства">
                    </c:if>
                    <div class="card-body">
                        <p class="text-success small fw-semibold">Есть в аптечке</p>
                        <h6 class="fw-bold">
                            <a href="${pageContext.request.contextPath}/pharmacies/detail/${pharmacy.id}"
                               class="text-decoration-none text-dark">
                                    ${pharmacy.medication.name}
                            </a>
                        </h6>
                        <ul class="list-unstyled small text-muted">
                            <li>Дата покупки: ${pharmacy.purchaseDate}</li>
                            <li>Срок годности: ${pharmacy.expirationDate}</li>
                            <li>Количество: ${pharmacy.quantity} шт</li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Заканчивается срок годности -->
    <h4 class="fw-bold mb-4">Заканчивается срок годности</h4>
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4 mb-5">
        <c:forEach var="pharmacy" items="${pharmacyPage.expiringSoon}">
            <div class="col">
                <div class="card h-100 shadow-sm">
                    <c:if test="${not empty pharmacy.medication.image}">
                        <img src="${pageContext.request.contextPath}/uploads${pharmacy.medication.image}"
                             class="card-img-fixed" alt="Фото лекарства">
                    </c:if>
                    <div class="card-body">
                        <p class="text-success small fw-semibold">Есть в аптечке</p>
                        <h6 class="fw-bold">
                            <a href="${pageContext.request.contextPath}/pharmacies/detail/${pharmacy.id}"
                               class="text-decoration-none text-dark">
                                    ${pharmacy.medication.name}
                            </a>
                        </h6>
                        <ul class="list-unstyled small text-muted">
                            <li>Дата покупки: ${pharmacy.purchaseDate}</li>
                            <li>Срок годности: ${pharmacy.expirationDate}</li>
                            <li>Количество: ${pharmacy.quantity} шт</li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
