#!/usr/bin/env groovy

pipeline {
    agent any

    stages {

        stage('checkout') {
            steps {
                checkout scm
            }
        }

        stage('check java') {
            steps {
                sh "java -version"
            }
        }

        stage('clean') {
            steps {
                sh "chmod +x mvnw"
                sh "./mvnw clean"
            }
        }

        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh "./mvnw test"
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
