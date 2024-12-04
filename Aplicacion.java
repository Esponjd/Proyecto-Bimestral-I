//Paquetes necesarios
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//Interfaz funcional para filtrar productos
@FunctionalInterface
interface FiltroProducto {
    boolean esFiltrado(Producto producto);
}

//Clase abstracta para productos
abstract class ProductoBase {

    //Atritbutos
    protected String codigo;
    protected String nombre;
    protected String presentacion;
    protected String grupo;
    protected String categoria;
    protected String marca;
    protected double costo;
    protected double precioVenta;
    protected double descuento;
    protected int stock;

    //Método constructor
    public ProductoBase(String codigo, String nombre, String presentacion, String grupo, String categoria, String marca,
                        double costo, double precioVenta, double descuento, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.grupo = grupo;
        this.categoria = categoria;
        this.marca = marca;
        this.costo = costo;
        this.precioVenta = precioVenta;
        this.descuento = descuento;
        this.stock = stock;
    }

    //Getters
    public String getCodigo() { 
        return codigo; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public String getGrupo() { 
        return grupo; 
    }

    public String getCategoria() { 
        return categoria; 
    }

    public double getPrecioVenta() { 
        return precioVenta; 
    }

    public int getStock() { 
        return stock; 
    }

    public String getPresentacion() {
        return presentacion;
    }

    //Método adicional
    @Override
    public String toString() {
        return "Producto [Código=" + codigo + ", Nombre=" + nombre + ", Presentación=" + presentacion +
               ", Grupo=" + grupo + ", Categoría=" + categoria + ", Marca=" + marca +
               ", Costo=" + costo + ", Precio de venta=" + precioVenta + ", Descuento=" + descuento +
               ", Stock=" + stock + "]";
    }
}

//Clase concreta para productos
class Producto extends ProductoBase {
    public Producto(String codigo, String nombre, String presentacion, String grupo, String categoria, String marca,
                    double costo, double precioVenta, double descuento, int stock) {

        super(codigo, nombre, presentacion, grupo, categoria, marca, costo, precioVenta, descuento, stock);

    }
}

//Clase para gestionar los productos
class GestorDeProductos {

    //Atributo
    public ArrayList<Producto> productos = new ArrayList<>();

    //Método para cargar el archivo .csv
    public void cargarDesdeCSV(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            br.readLine(); //Saltar la cabecera
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                String codigo = datos[0];
                String nombre = datos[1];
                String presentacion = datos[2];
                String grupo = datos[3];
                String categoria = datos[4];
                String marca = datos[5];
                double costo = Double.parseDouble(datos[6]);
                double precioVenta = Double.parseDouble(datos[7]);
                double descuento = Double.parseDouble(datos[8]);
                int stock = Integer.parseInt(datos[9]);

                Producto producto = new Producto(codigo, nombre, presentacion, grupo, categoria, marca, costo, precioVenta, descuento, stock);
                productos.add(producto);
            }
            System.out.println("Datos cargados exitosamente desde el archivo CSV.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    //Método para mostrar productos
    public void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            productos.forEach(System.out::println);
        }
    }

    //Método para consultar por rango de precios
    public void consultarPorRangoDePrecios(double min, double max) {
        List<Producto> resultado = productos.stream()
                .filter(p -> p.getPrecioVenta() >= min && p.getPrecioVenta() <= max)
                .collect(Collectors.toList());
        if (resultado.isEmpty()) {
            System.out.println("No se encontraron productos en el rango de precios especificado.");
        } else {
            resultado.forEach(System.out::println);
        }
    }

    //Método para consultar por grupo o categoría
    public void consultarPorCriterio(String criterio, String valor) {
        Predicate<Producto> esFiltrado = p -> (criterio.equalsIgnoreCase("grupo") && p.getGrupo().equalsIgnoreCase(valor)) ||
                                        (criterio.equalsIgnoreCase("categoria") && p.getCategoria().equalsIgnoreCase(valor));
        List<Producto> resultado = productos.stream().filter(esFiltrado).collect(Collectors.toList());
        if (resultado.isEmpty()) {
            System.out.println("No se encontraron productos para el criterio especificado.");
        } else {
            resultado.forEach(System.out::println);
        }
    }

    //Método para consultar Stock
    public void consultarStock() {
        productos.forEach(p -> System.out.println("Código: " + p.getCodigo()+ ", Nombre: "+ p.getNombre() +  ", Presentación: "+ p.getPresentacion() +", Stock: " + p.getStock()));
    }

    //Método para agregar producto
    public void agregarProducto(Producto producto) {
        productos.add(producto);
        System.out.println("Producto agregado exitosamente.");
    }

    //Método para actualizar producto (existente)
    public void actualizarProducto(String codigo, Producto nuevoProducto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo().equals(codigo)) {
                productos.set(i, nuevoProducto);
                System.out.println("Producto actualizado exitosamente.");
                return;
            }
        }
        System.out.println("Producto no encontrado.");
    }

    //Método para eliminar producto
    public void eliminarProducto(String codigo) {
        boolean eliminado = productos.removeIf(p -> p.getCodigo().equals(codigo));
        if (eliminado) {
            System.out.println("Producto eliminado exitosamente.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
}

public class Aplicacion {
    public static void main(String[] args) {
        GestorDeProductos gestor = new GestorDeProductos();
        gestor.cargarDesdeCSV("Productos.csv"); //Ruta al archivo CSV

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;  //Atributo para el scanner
        do {
            try {
                System.out.println("\n=== Menú Principal ===");
                System.out.println("1. Mostrar todos los productos");
                System.out.println("2. Consultar productos por rango de precios");
                System.out.println("3. Consultar productos por grupo o categoría");
                System.out.println("4. Consultar stock de productos");
                System.out.println("5. Agregar un nuevo producto");
                System.out.println("6. Actualizar un producto existente");
                System.out.println("7. Eliminar un producto");
                System.out.println("8. Salir");
                System.out.print("Ingrese una opción: ");
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1 -> gestor.mostrarProductos();
                   case 2 -> {
                        try {
                            System.out.println("Utilice (,) como separador decimal");
                            System.out.print("Ingrese precio mínimo: ");
                            double min = scanner.nextDouble();
                            System.out.print("Ingrese precio máximo: ");
                            double max = scanner.nextDouble();
                            
                            // Validar que los precios no sean negativos
                            if (min < 0 || max < 0) {
                                System.err.println("Error: Los precios no pueden ser negativos. Intente nuevamente.");
                            } else if (min > max) {
                                System.err.println("Error: El precio mínimo no puede ser mayor que el precio máximo.");
                            } else {
                                gestor.consultarPorRangoDePrecios(min, max);
                            }
                        } catch (InputMismatchException e) {
                            System.err.println("Error: Asegúrese de ingresar valores numéricos válidos.");
                            scanner.nextLine();
                        }
                    }
                    case 3 -> {
                        try {
                            System.out.print("Consultar por (grupo/categoria): ");
                            String criterio = scanner.next().trim().toLowerCase();
                            
                            //Validar entrada válida
                            if (!criterio.equals("grupo") && !criterio.equals("categoria")) {
                                throw new IllegalArgumentException("El criterio ingresado no es válido. Debe ser 'grupo' o 'categoria'.");
                            }
                    
                            scanner.nextLine(); //Crear el salto de linea
                            System.out.print("Ingrese lo que desea buscar (utilice tildes segun corresponda): ");
                            String valor = scanner.nextLine().trim(); // Limpieza de espacios en la entrada
                            
                            // Validar que que no sea vacío
                            if (valor.isEmpty()) {
                                throw new IllegalArgumentException("El valor ingresado no puede estar vacío.");
                            }
                    
                            gestor.consultarPorCriterio(criterio, valor);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Error: " + e.getMessage());
                        } catch (Exception e) {
                            System.err.println("Error inesperado: " + e.getMessage());
                        }
                    }
                    
                    case 4 -> gestor.consultarStock();
                    case 5 -> {
                        System.out.println("Utilice (;)para separar los datos");
                        System.out.println("Utilice (.) como separador decimal");
                        System.out.print("Ingrese los datos del nuevo producto (Código;Nombre;Presentación;Grupo;Categoría;Marca;Costo;PrecioVenta;Descuento;Stock): ");
                        scanner.nextLine();
                        try {
                            String[] datos = scanner.nextLine().split(";");
                            Producto nuevoProducto = new Producto(
                                    datos[0], datos[1], datos[2], datos[3], datos[4], datos[5],
                                    Double.parseDouble(datos[6]), Double.parseDouble(datos[7]),
                                    Double.parseDouble(datos[8]), Integer.parseInt(datos[9])
                            );
                            gestor.agregarProducto(nuevoProducto);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.err.println("Error: Asegúrese de ingresar los datos en el formato correcto.");
                        }
                    }
                    case 6 -> {
                        System.out.print("Ingrese el código del producto a actualizar: ");
                        String codigo = scanner.next();
                    
                        //Verificación de la existencia del código antes de pedir los demás datos
                        Producto productoExistente = null;
                        for (Producto producto : gestor.productos) {
                            if (producto.getCodigo().equals(codigo)) {
                                productoExistente = producto;
                                break;
                            }
                        }
                        if (productoExistente == null) {
                            System.out.println("Producto no encontrado.");
                        } else {
                            System.out.println("Producto encontrado. Proceda a ingresar los nuevos datos.");
                            System.out.println("Utilice (;)para separar los datos");
                            System.out.println("Utilice (.) como separador decimal");
                            System.out.print("Ingrese los datos del producto actualizado (Código;Nombre;Presentación;Grupo;Categoría;Marca;Costo;PrecioVenta;Descuento;Stock): ");
                            scanner.nextLine(); 
                    
                            try {
                                String[] datos = scanner.nextLine().split(";");
                                Producto nuevoProducto = new Producto(
                                        datos[0], datos[1], datos[2], datos[3], datos[4], datos[5],
                                        Double.parseDouble(datos[6]), Double.parseDouble(datos[7]),
                                        Double.parseDouble(datos[8]), Integer.parseInt(datos[9])
                                );
                                gestor.actualizarProducto(codigo, nuevoProducto);
                            } catch (Exception e) {
                                System.err.println("Error al actualizar el producto: Asegúrese de ingresar los datos en el formato correcto.");
                            }
                        }
                    }
                    
                    case 7 -> {
                        System.out.print("Ingrese el código del producto a eliminar: ");
                        String codigo = scanner.next();
                        gestor.eliminarProducto(codigo);
                    }
                    case 8 -> System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    default -> System.err.println("Opción inválida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Debe ingresar un número para la opción.");
                scanner.nextLine();
            }
        } while (opcion != 8);

        scanner.close();
    }
}

