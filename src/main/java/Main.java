

import java.util.Scanner;
import java.util.InputMismatchException;
import java.sql.SQLException;

import model.Usuarios;
import repository.UsuariosRepository;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuariosRepository repo = new UsuariosRepository();
        int opcion;

        do {
            System.out.println("Menú de opciones:");
            System.out.println("1. Insertar usuario");
            System.out.println("2. Mostrar usuarios");
            System.out.println("3. Buscar usuario por ID");
            System.out.println("4. Eliminar usuario");
            System.out.println("5. Actualizar usuario");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    // Pide datos y llama a repo.insertarUsuario(...)
                    scanner.nextLine(); // Consumir la nueva línea
                    UsuariosRepository usuariosRepository = new UsuariosRepository();

                    System.out.print("Ingrese el nombre: ");
                    String nombre = scanner.nextLine();

                    System.out.print("Ingrese la edad: ");
                    Long edad = scanner.nextLong();

                    Usuarios usuario = new Usuarios(nombre, edad);
                    usuariosRepository.insertarUsuario(usuario);
                    
                
                    break;
                case 2:
                    Scanner scanner2 = new Scanner(System.in);
                    System.out.print("deseas ver los usuarios (s/n): ");
                    String respuesta = scanner2.nextLine();
                    if (respuesta.equalsIgnoreCase("s")) {
                        try {
                            repo.mostrarUsuario();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("No se mostrarán los usuarios.");
                    }
                   
                    break;
                case 3:
                    System.out.print("Ingrese el id que desea buscar: ");
                    Long id = scanner.nextLong();
                    if (id != null && id >= 1) {
                        try {
                            Usuarios encontrado = repo.obtenerUsuarioPorId(id);
                            if (encontrado != null) {
                                System.out.println("Usuario encontrado: ID=" + encontrado.getId()
                                        + ", Nombre=" + encontrado.getNombre()
                                        + ", Edad=" + encontrado.getEdad());
                            } else {
                                System.out.println("No se encontró ningún usuario con id " + id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Id inválido.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese el id que desea eliminar: ");
                    long idU;
                    try {
                        idU = scanner.nextLong();
                    } catch (InputMismatchException ime) {
                        System.out.println("Id inválido.");
                        scanner.nextLine(); // consumir token inválido
                        break;
                    }

                    if (idU >= 1) {
                        try {
                            boolean eliminado = repo.eliminarUsuario(idU);
                            if (eliminado) {
                                System.out.println("Usuario eliminado correctamente. id=" + idU);
                            } else {
                                System.out.println("No se encontró ningún usuario con id " + idU);
                            }
                        } catch (SQLException e) {
                            System.err.println("Error al eliminar usuario: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Id inválido.");
                    }
                    break;

                case 5:
                    System.out.print("Ingrese el id del usuario a actualizar: ");
                    long idToUpdate;
                    try {
                        idToUpdate = scanner.nextLong();
                    } catch (InputMismatchException ime) {
                        System.out.println("Id inválido.");
                        scanner.nextLine();
                        break;
                    }
                    if (idToUpdate < 1) {
                        System.out.println("Id inválido.");
                        break;
                    }
                    scanner.nextLine(); 
                    System.out.print("Nuevo nombre: ");
                    String nuevoNombre = scanner.nextLine().trim();
                    System.out.print("Nueva edad: ");
                    long nuevaEdad;
                    try {
                        nuevaEdad = scanner.nextLong();
                    } catch (InputMismatchException ime) {
                        System.out.println("Edad inválida.");
                        scanner.nextLine();
                        break;
                    }

                    Usuarios toUpdate = new Usuarios(idToUpdate, nuevoNombre, nuevaEdad);
                    try {
                        boolean updated = repo.actualizarUsuario(toUpdate);
                        if (updated) {
                            System.out.println("Usuario actualizado correctamente: ID=" + toUpdate.getId()
                                + ", Nombre=" + toUpdate.getNombre()
                                + ", Edad=" + toUpdate.getEdad());
                        } else {
                            System.out.println("No existe usuario con id " + idToUpdate);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error al actualizar usuario: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);

        scanner.close();
    }
}
