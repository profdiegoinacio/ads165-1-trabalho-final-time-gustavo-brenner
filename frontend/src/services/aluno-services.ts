import {Aluno} from "@/app/types/aluno";

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export const AlunoService = {
    /**
     * Busca todos os alunos disponíveis na API
     */
    async listarTodos(): Promise<Aluno[]> {
        const response = await fetch(`${API_URL}/alunos`);

        if (!response.ok) {
            throw new Error(`Erro ao buscar alunos: ${response.status}`);
        }

        return response.json();
    },

    /**
     * Busca um aluno específico pelo ID
     */
    async buscarPorId(id: number): Promise<Aluno> {
        const response = await fetch(`${API_URL}/alunos/${id}`);

        if (!response.ok) {
            throw new Error(`Erro ao buscar aluno: ${response.status}`);
        }

        return response.json();
    },

    /**
     * Cria um novo aluno
     */
    async criar(aluno: Aluno): Promise<Aluno> {
        const response = await fetch(`${API_URL}/alunos`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(aluno),
        });

        if (!response.ok) {
            throw new Error(`Erro ao criar aluno: ${response.status}`);
        }

        return response.json();
    },

    /**
     * Atualiza um aluno existente
     */
    async atualizar(aluno: Aluno): Promise<Aluno> {
        if (!aluno.id) {
            throw new Error('ID do aluno é obrigatório para atualização');
        }

        const response = await fetch(`${API_URL}/alunos/${aluno.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(aluno),
        });

        if (!response.ok) {
            throw new Error(`Erro ao atualizar aluno: ${response.status}`);
        }

        return response.json();
    },

    /**
     * Remove um aluno pelo ID
     */
    async excluir(id: number): Promise<void> {
        const response = await fetch(`${API_URL}/alunos/${id}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error(`Erro ao excluir aluno: ${response.status}`);
        }
    }
};