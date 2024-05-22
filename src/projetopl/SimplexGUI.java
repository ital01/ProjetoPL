package projetopl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimplexGUI extends JFrame {
    private JTextField[][] coeficientes;
    private JPanel inputPanel;
    private JButton calcularButton, addRowButton, addColumnButton, removeRowButton, removeColumnButton;
    private JTextArea resultadoArea;
    private int rows = 4;
    private int cols = 4;

    public SimplexGUI() {
        setTitle("Método Simplex");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        coeficientes = new JTextField[rows][cols];
        initializeTable();

        JPanel buttonPanel = new JPanel();
        calcularButton = new JButton("Calcular");
        calcularButton.addActionListener(new CalcularListener());
        addRowButton = new JButton("Adicionar Linha");
        addRowButton.addActionListener(new AddRowListener());
        addColumnButton = new JButton("Adicionar Coluna");
        addColumnButton.addActionListener(new AddColumnListener());
        removeRowButton = new JButton("Remover Linha");
        removeRowButton.addActionListener(new RemoveRowListener());
        removeColumnButton = new JButton("Remover Coluna");
        removeColumnButton.addActionListener(new RemoveColumnListener());

        buttonPanel.add(calcularButton);
        buttonPanel.add(addRowButton);
        buttonPanel.add(addColumnButton);
        buttonPanel.add(removeRowButton);
        buttonPanel.add(removeColumnButton);

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultadoArea), BorderLayout.SOUTH);
    }

    private void initializeTable() {
        inputPanel.removeAll();
        inputPanel.setLayout(new GridLayout(rows, cols, 10, 10));
        coeficientes = new JTextField[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                coeficientes[i][j] = new JTextField(5);
                inputPanel.add(coeficientes[i][j]);
            }
        }
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private class AddRowListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            rows++;
            initializeTable();
        }
    }

    private class AddColumnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cols++;
            initializeTable();
        }
    }

    private class RemoveRowListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (rows > 1) {
                rows--;
                initializeTable();
            } else {
                JOptionPane.showMessageDialog(SimplexGUI.this, "Não é possível remover todas as linhas.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RemoveColumnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cols > 1) {
                cols--;
                initializeTable();
            } else {
                JOptionPane.showMessageDialog(SimplexGUI.this, "Não é possível remover todas as colunas.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class CalcularListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                float[][] padronizado = new float[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        padronizado[i][j] = Float.parseFloat(coeficientes[i][j].getText());
                    }
                }

                Simplex simplex = new Simplex(rows - 1, cols - 1);
                simplex.preencherTabela(padronizado);

                boolean sair = false;
                resultadoArea.append("---Conjunto inicial---\n");
                printTabela(simplex.getTabela());

                while (!sair) {
                    Simplex.ERRO err = simplex.calcular();

                    if (err == Simplex.ERRO.OTIMO) {
                        resultadoArea.append("---Solução Ótima Encontrada---\n");
                        printTabela(simplex.getTabela());
                        sair = true;
                    } else if (err == Simplex.ERRO.INDETERMIANDA) {
                        resultadoArea.append("---Solução é Indeterminada---\n");
                        sair = true;
                    } else {
                        resultadoArea.append("---Iteração---\n");
                        printTabela(simplex.getTabela());
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(SimplexGUI.this, "Por favor, insira números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void printTabela(float[][] tabela) {
            for (float[] linha : tabela) {
                for (float valor : linha) {
                    resultadoArea.append(String.format("%.2f\t", valor));
                }
                resultadoArea.append("\n");
            }
            resultadoArea.append("\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimplexGUI frame = new SimplexGUI();
                frame.setVisible(true);
            }
        });
    }
}