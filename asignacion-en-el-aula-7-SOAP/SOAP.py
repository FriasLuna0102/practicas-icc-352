import json
from zeep import Client
import requests

# URL del archivo WSDL del servicio web SOAP
url_wsdl = 'http://localhost:7000/ws/EstudianteWebServices?wsdl'

# Crear un cliente SOAP
cliente = Client(url_wsdl)


class Estudiante:
    def __init__(self, matricula, nombre, carrera):
        self.matricula = matricula
        self.nombre = nombre
        self.carrera = carrera


def listar_estudiantes():
    # Llamar al método del servicio SOAP para listar estudiantes
    resultado = cliente.service.getListaEstudiante()

    # Mostrar los nombres de los estudiantes
    print("Listando todos los estudiantes:")
    for estudiante in resultado:
        if hasattr(estudiante, 'nombre'):
            print(estudiante.matricula)
            print(estudiante.nombre)
            print(estudiante.carrera)
            print("\n")
        else:
            print("Nombre no disponible para este estudiante")


def consultar_estudiantes():
    matricula = input("Introduce la matrícula del estudiante: ")

    try:
        # Llamar al método del servicio SOAP para obtener los detalles del estudiante
        estudiante = cliente.service.getEstudiante(matricula)

        # Mostrar los detalles del estudiante
        if hasattr(estudiante, 'nombre'):
            print("Matrícula:", estudiante.matricula)
            print("Nombre:", estudiante.nombre)
            print("Carrera:", estudiante.carrera)
        else:
            print("Estudiante no encontrado.")
    except Exception as e:
        print("Error al consultar el estudiante:", str(e))


def crear_estudiante():
    matricula = int(input("Matrícula: "))
    nombre = input("Nombre: ")
    carrera = input("Carrera: ")

    try:
        # Crear un diccionario con los datos del estudiante
        estudiante_data = {'matricula': matricula, 'nombre': nombre, 'carrera': carrera}

        # Llamar al método del servicio SOAP para crear un estudiante
        resultado = cliente.service.crearEstudiante(estudiante_data)

        # Mostrar el resultado
        print("Creando un nuevo estudiante:", resultado)
    except Exception as e:
        print("Error al crear el estudiante:", str(e))


def borrar_estudiante():
    matricula = input("Introduce la matrícula del estudiante a borrar: ")

    try:
        estudiante = cliente.service.getEstudiante(matricula)
        cliente.service.eliminarEstudiante(estudiante)

        print("Estudiante borrado exitosamente.")

    except Exception as e:
        print("Error al borrar el estudiante:", str(e))


# Mapea las opciones a las funciones correspondientes
opciones = {
    1: listar_estudiantes,
    2: consultar_estudiantes,
    3: crear_estudiante,
    4: borrar_estudiante
}

while True:
    print("""
    Por favor, selecciona una opción:
    1. Listar todos los estudiantes
    2. Consultar estudiantes
    3. Crear un nuevo estudiante
    4. Borrar un estudiante
    5. Salir
    """)

    opcion = int(input("Opción: "))

    if opcion == 5:
        break  # Salir del bucle

    if opcion in opciones:
        # Llama a la función correspondiente
        opciones[opcion]()

    else:
        print("Opción no válida. Por favor, intenta de nuevo.")
