function fetchDeudas() {
    fetch('/api/deudas/mes/nopagadas')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const deudasTable = document.getElementById('deudasTable');
            const alertContainer = document.getElementById('alertContainer');
            const nombreMes = document.getElementById('nombreMes');
            const today = new Date();
            const dayOfWeek = today.getDay(); // 0 (Sunday) to 6 (Saturday)
            const startOfWeek = new Date(today);
            startOfWeek.setDate(today.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1)); // Monday of the current week
            const endOfWeek = new Date(startOfWeek);
            endOfWeek.setDate(startOfWeek.getDate() + 6); // Sunday of the current week

            const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
            const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);

            const monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
            nombreMes.textContent = monthNames[today.getMonth()];

            // Limpia la tabla y el contenedor de alertas antes de agregar nuevas filas
            deudasTable.querySelector('tbody').innerHTML = '';
            alertContainer.innerHTML = '';

            data.forEach(deuda => {
                const row = document.createElement('tr');
                const fechaVencimiento = new Date(deuda.fechaVencimiento);

                // Aplicar clases según los criterios
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

                // Formatear monto como dinero
                const montoFormateado = new Intl.NumberFormat('es-PE', { style: 'currency', currency: 'PEN' }).format(deuda.monto);

                row.innerHTML = `
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

                deudasTable.querySelector('tbody').appendChild(row);

                // Mostrar alerta si la deuda vence hoy
                if (fechaVencimiento.toISOString().split('T')[0] === today.toISOString().split('T')[0] && !deuda.estaPagado) {
                    const alert = document.createElement('div');
                    alert.classList.add('alert', 'alert-danger');
                    alert.innerHTML = `
                        Alerta: La deuda con número de documento ${deuda.numeroDocumento} de la empresa ${deuda.nombreEmpresa} vence hoy.
                    `;
                    alertContainer.appendChild(alert);
                }
            });
        })
        .catch(error => console.error('Error fetching deudas:', error));
}

function marcarComoPagada(deudaId) {
    fetch(`/api/deudas/${deudaId}/pagar`, { method: 'PUT' })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            fetchDeudas(); // Refresca la tabla después de marcar como pagada
        })
        .catch(error => console.error('Error marking deuda as paid:', error));
}

function redirigirRegistro() {
    window.location.href = '/registrar_deuda.html';
}

// Llamar a la función fetchDeudas al cargar la página
window.onload = fetchDeudas;