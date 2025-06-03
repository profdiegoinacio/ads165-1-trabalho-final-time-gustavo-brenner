'use client';

import { signIn } from 'next-auth/react';
import { useRouter, useSearchParams } from 'next/navigation';
import React, { useState, FormEvent } from 'react';

export default function LoginPage() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const callbackUrl = searchParams.get('callbackUrl') || '/dashboard'; // Rota padrão após login

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError(null);

        try {
            const result = await signIn('credentials', {
                redirect: false, // Não redirecionar automaticamente, lidaremos com isso
                username,
                password,
                callbackUrl,
            });

            if (result?.error) {
                setError('Invalid username or password. Please try again.'); // Mensagem de erro genérica
                console.error('Sign in error:', result.error);
            } else if (result?.ok) {
                router.push(callbackUrl); // Redireciona após login bem-sucedido
            }
        } catch (e) {
            setError('An unexpected error occurred. Please try again.');
            console.error('Sign in exception:', e);
        }
    };

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="username">Username:</label>
                    <input
                        id="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">Password:</label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <button type="submit">Login</button>
            </form>
            {/* Opcional: link para registro */}
            {/* <p>Don't have an account? <a href="/register">Register here</a></p> */}
        </div>
    );
}