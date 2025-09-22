#!/bin/bash

# Script para construir y ejecutar la aplicación con Docker

echo "🐳 Construyendo imagen Docker..."

# Construir la imagen
docker build -t private-proyect:latest .

if [ $? -eq 0 ]; then
    echo "✅ Imagen construida exitosamente"
    
    echo "🚀 Iniciando contenedores con docker-compose..."
    
    # Detener contenedores existentes
    docker-compose down
    
    # Construir y ejecutar
    docker-compose up --build -d
    
    if [ $? -eq 0 ]; then
        echo "✅ Aplicación iniciada exitosamente"
        echo "🌐 Aplicación disponible en: http://localhost:8080"
        echo "📊 Base de datos PostgreSQL en: localhost:5432"
        echo ""
        echo "📋 Comandos útiles:"
        echo "  - Ver logs: docker-compose logs -f app"
        echo "  - Detener: docker-compose down"
        echo "  - Reiniciar: docker-compose restart app"
    else
        echo "❌ Error al iniciar la aplicación"
        exit 1
    fi
else
    echo "❌ Error al construir la imagen"
    exit 1
fi
