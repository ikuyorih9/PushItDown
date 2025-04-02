export const SLIDELOGS_FRAMES = [
    {
      title: "Todos os registros",
      filterFields : [
        { label: 'Data', type: 'date', name: 'data' },
        { label: 'Tipo', type: 'text', name: 'tipo' },
        { label: 'Período', type: 'text', name: 'periodo' }
      ],
      tableType: 'all',
      tableColumns : ['Data', 'Hora', 'Tipo']
    },
    {
      title: "Registros por dia",
      filterFields : [
        { label: 'Mês', type: 'month', name: 'mes' },
        { label: 'Período', type: 'text', name: 'periodo' }
      ],
      tableType: 'days',
      tableColumns : ['Data', 'Expediente']
    }

  ];