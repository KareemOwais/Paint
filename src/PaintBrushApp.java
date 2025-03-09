import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

class PaintBrushApp {
    private JFrame frame;
    private JPanel toolbar, colorPanel, topPanel;
    private DrawingPanel drawingPanel;

    public PaintBrushApp() {
        frame = new JFrame("Paint Brush");
        frame.setSize(1024, 1024);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        topPanel = new JPanel(new GridLayout(1, 2));

        // Toolbar
        toolbar = new JPanel(new GridLayout(2, 5, 5, 5));
        addToolButton("Rectangle");
        addToolButton("Oval");
        addToolButton("Triangle");
        addToolButton("Line");
        addToolButton("Pencil");
        JToggleButton fillButton = new JToggleButton("Fill"); //create an object of toggle button to allow fill for any shape
        fillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setFillMode(fillButton.isSelected());
            }
        });
        toolbar.add(fillButton);
        JToggleButton dottedButton = new JToggleButton("Dotted"); //create an object of toggle button to allow fill for any shape
        dottedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setDotted(dottedButton.isSelected());
            }
        });
        toolbar.add(dottedButton);
        addEraserlist();
        addToolButton("Eraser");
        addToolButton("Clear");
        addToolButton("Undo");
        addToolButton("Redo");
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    BufferedImage image = drawingPanel.getDrawingImage();
                    try{
                        ImageIO.write(image, "PNG", file);
                    }
                    catch (IOException s) {
                        // Handle the IOException
                        System.out.println(" " + s.getMessage());
                    }
                }
            }
        });
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BufferedImage loadedImage = ImageIO.read(file); // Read the image
                        drawingPanel.loadImage(loadedImage);
                    } catch (IOException ex) {
                        System.out.println(" " + ex.getMessage());
                    }
                }
            }
        });
        toolbar.add(loadButton);

        toolbar.add(saveButton);

        // Color Panel
        colorPanel = new JPanel(new FlowLayout());
        addColorButton(Color.RED);
        addColorButton(Color.GREEN);
        addColorButton(Color.BLUE);
        addColorButton(Color.YELLOW);
        addColorButton(Color.BLACK);
        addColorButton(Color.ORANGE);
        addColorButton(Color.PINK);
        addColorButton(Color.CYAN);

        topPanel.add(toolbar);
        topPanel.add(colorPanel);

        drawingPanel = new DrawingPanel();

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(drawingPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    private void addEraserlist(){
        JButton strokeButton = new JButton("EraserSize");
        JPopupMenu popupMenu = new JPopupMenu();

        for(int i =10 ; i<=60;i=i+10){
            JMenuItem item = new JMenuItem(Integer.toString(i));
            final int currentI = i;
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawingPanel.setStroke(currentI);
                }
            });
            popupMenu.add(item);
        }
        strokeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.show(strokeButton,50,25); //set invoker as the Stroke Button
            }
        });
        toolbar.add(strokeButton);
    }
    private void addToolButton(String toolName) {
        JButton button = new JButton(toolName);
        switch (toolName) {
            case "Clear" -> button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawingPanel.clear();
                }
            });
            case "Undo" -> button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawingPanel.undo();
                }
            });
            case "Redo" -> button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawingPanel.redo();
                }
            });
            default -> button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawingPanel.setSelectedTool(toolName);
                }
            });
        }
        toolbar.add(button);

    }

    private void addColorButton(Color color) {
        JButton colorBtn = new JButton();
        colorBtn.setBackground(color);
        colorBtn.setPreferredSize(new Dimension(30, 30));
        colorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setSelectedColor(color);
            }
        });
        colorPanel.add(colorBtn);
    }

    public static void main(String[] args) {
        new PaintBrushApp();
    }
}