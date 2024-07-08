// Variables globales para controlar el mes actual
let currentMonth = new Date().getMonth();
let currentYear = new Date().getFullYear();

// Función para obtener deudas desde el backend
function fetchDeudas(mes, anio) {
    const today = new Date();
    const currentMonth = today.getMonth() + 1; // getMonth() devuelve 0 para enero, por eso se suma 1

    if (!mes || !anio || (mes === currentMonth && anio === today.getFullYear())) {
        fetch('/api/deudas/mes/nopagadas')
            .then(handleFetchResponse)
            .then(processDeudas)
            .catch(error => console.error('Error fetching deudas:', error));
    } else {
        fetch(`/api/deudas/por-mes?mes=${mes}&anio=${anio}`)
            .then(handleFetchResponse)
            .then(processDeudas)
            .catch(error => console.error('Error fetching deudas:', error));
    }
}


// Maneja la respuesta de la solicitud fetch
function handleFetchResponse(response) {
    if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
    }
    return response.json();
}

// Procesa las deudas obtenidas del backend
function processDeudas(data) {
    const deudasTable = document.getElementById('deudasTable');
    const alertContainer = document.getElementById('alertContainer');
    const nombreMes = document.getElementById('nombreMes');
    const today = new Date();
    const startOfWeek = getStartOfWeek(today);
    const endOfWeek = getEndOfWeek(startOfWeek);
    const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
    const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);

    const monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
    nombreMes.textContent = monthNames[currentMonth];

    // Limpia la tabla y el contenedor de alertas antes de agregar nuevas filas
    deudasTable.querySelector('tbody').innerHTML = '';
    alertContainer.innerHTML = '';

    data.forEach(deuda => {
        const row = document.createElement('tr');
        const fechaVencimiento = new Date(deuda.fechaVencimiento);

        // Aplicar clases según los criterios
        applyRowClasses(row, deuda, fechaVencimiento, today, startOfWeek, endOfWeek, startOfMonth, endOfMonth);

        // Formatear monto como dinero
        const montoFormateado = formatCurrency(deuda.monto);

        row.innerHTML = generateRowContent(deuda, fechaVencimiento, montoFormateado, today);

        deudasTable.querySelector('tbody').appendChild(row);

        // Mostrar alerta si la deuda vence hoy
        if (isDueToday(fechaVencimiento, today, deuda)) {
            showDueTodayAlert(alertContainer, deuda);
        }
    });
}

// Obtiene el inicio de la semana actual
function getStartOfWeek(today) {
    const dayOfWeek = today.getDay(); // 0 (Sunday) to 6 (Saturday)
    const startOfWeek = new Date(today);
    startOfWeek.setDate(today.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1)); // Monday of the current week
    return startOfWeek;
}

// Obtiene el final de la semana actual
function getEndOfWeek(startOfWeek) {
    const endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(startOfWeek.getDate() + 6); // Sunday of the current week
    return endOfWeek;
}

// Aplica las clases necesarias a la fila de la deuda
function applyRowClasses(row, deuda, fechaVencimiento, today, startOfWeek, endOfWeek, startOfMonth, endOfMonth) {
    row.className = ''; // Reset classes
    if (!deuda.estaPagado && fechaVencimiento < today) {
        row.classList.add('deuda-vencida');
    } else if (deuda.estaPagado) {
        row.classList.add('deuda-pagada');
    } else if (fechaVencimiento >= startOfWeek && fechaVencimiento <= endOfWeek) {
        row.classList.add('deuda-semana-actual');
    } else if (fechaVencimiento >= startOfMonth && fechaVencimiento <= endOfMonth) {
        row.classList.add('deuda-mes-actual');
    }
}

// Formatea un monto como moneda
function formatCurrency(monto) {
    return new Intl.NumberFormat('es-PE', { style: 'currency', currency: 'PEN' }).format(monto);
}

// Genera el contenido HTML para una fila de deuda
function generateRowContent(deuda, fechaVencimiento, montoFormateado, today) {
    return `
        <td>${deuda.numeroDocumento}</td>
        <td>${deuda.nombreEmpresa}</td>
        <td>${fechaVencimiento.toISOString().split('T')[0]}</td>
        <td>${montoFormateado}</td>
        <td class="estado-pagado">
            ${deuda.estaPagado ? '<i class="fas fa-check"></i>' : (fechaVencimiento < today && !deuda.estaPagado ? 'VENCIDA' : '<i class="fas fa-minus"></i>')}
        </td>
        <td>
            ${!deuda.estaPagado ? `<button class="btn" onclick="marcarComoPagada(${deuda.id})">Marcar como Pagada</button>` : '<button class="btn" disabled>Pagado</button>'}
        </td>
    `;
}

// Verifica si una deuda vence hoy
function isDueToday(fechaVencimiento, today, deuda) {
    return fechaVencimiento.toISOString().split('T')[0] === today.toISOString().split('T')[0] && !deuda.estaPagado;
}

// Muestra una alerta si una deuda vence hoy
function showDueTodayAlert(alertContainer, deuda) {
    const alert = document.createElement('div');
    alert.classList.add('alert', 'alert-danger');
    alert.innerHTML = `
        Alerta: La deuda con número de documento ${deuda.numeroDocumento} de la empresa ${deuda.nombreEmpresa} vence hoy.
    `;
    alertContainer.appendChild(alert);
}

// Marca una deuda como pagada
function marcarComoPagada(deudaId) {
    fetch(`/api/deudas/${deudaId}/pagar`, { method: 'PUT' })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            fetchDeudas(currentMonth + 1, currentYear); // Refresca la tabla después de marcar como pagada
        })
        .catch(error => console.error('Error marking deuda as paid:', error));
}

// Redirige a la página de registro de deuda
function redirigirRegistro() {
    window.location.href = '/html/registro_deuda.html';
}

function redirigirCronograma() {
    window.location.href = '/html/mostrar_cronograma.html';
}

function redirigirRegistroCronograma() {
    window.location.href = '/html/registro_cronograma.html';
}

// Función para avanzar al siguiente mes
function avanzarMes() {
    if (currentMonth < 11) {
        currentMonth++;
    } else {
        currentMonth = 0;
        currentYear++;
    }
    fetchDeudas(currentMonth + 1, currentYear);
}

// Función para retroceder al mes anterior, no retrocede antes del mes actual
function retrocederMes() {
    const today = new Date();
    const currentMonthNow = today.getMonth();
    const currentYearNow = today.getFullYear();

    if (currentMonth > currentMonthNow || currentYear > currentYearNow) {
        if (currentMonth > 0) {
            currentMonth--;
        } else {
            currentMonth = 11;
            currentYear--;
        }
        fetchDeudas(currentMonth + 1, currentYear);
    } else {
        fetchDeudas(currentMonth + 1, currentYear);
    }
}

// Llamar a la función fetchDeudas al cargar la página
window.onload = () => fetchDeudas(currentMonth + 1, currentYear);